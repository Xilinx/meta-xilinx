# Booting OS Images on ZynqMP target boards

Booting OS images on ZynqMP boards can be done using JTAG, SD, eMMC, QSPI and 
NAND boot modes.

* [Setting Up the Target](#setting-up-the-target)
* [Booting from JTAG](#booting-from-jtag)
  * [Sourcing the XSDB tools](#sourcing-the-xsdb-tools)
  * [Deploying the images to target](#deploying-the-images-to-target)
    * [Using devtool boot-jtag script](#using-devtool-boot-jtag-script)
    * [Manually executing xsdb commands](#manually-executing-xsdb-commands)
      * [Loading boot components using XSDB](#loading-boot-components-using-xsdb)
      * [Loading Kernel, Root Filesystem and U-boot boot script](#loading-kernel-root-filesystem-and-u-boot-boot-script)
        * [Using XSDB](#using-xsdb)
        * [Using TFTP](#using-tftp)
      * [Booting Linux](#booting-linux)
* [Booting from SD](#booting-from-sd)
* [Booting from QSPI](#booting-from-qspi)

## Setting Up the Target
1. Connect a USB cable between the CP2180 USB-to-Quad-UART bridge USB Micro-B on
   the target and the USB port on the host machine.
2. Connect a micro USB cable from the ZCU102 board USB UART port (J83) to the USB
   port on the host machine.
3. Default UART terminal(serial port) settings is Speed `115200`, Data `8 bit`,
   Parity `None`, Stop bits ` 1 bit` and Flow control `None`.
4. Set the board to JTAG and other boot mode by setting the boot mode switch by 
   referring to board user guide. For zynqmp-generic machine configuration
   file zcu102 evaluation board is used as reference and below is the
   configuration boot mode settings (SW6).

> **Note:** Switch OFF = 1 = High; ON = 0 = Low

| Boot Mode | Mode Pins [3:0] | Mode SW6 [3:0]    | Comments                  |
|-----------|-----------------|-------------------|---------------------------|
| JTAG      | 0000            | ON, ON, ON, ON    | PS JTAG                   |
| QSPI      | 0010            | ON, ON, OFF, ON   | QSPI 32-bit addressing    |
| SD        | 1110            | OFF, OFF, OFF, ON | SD 3.0 with level shifter |

## Booting from JTAG

This boot flow requires the use of the AMD Xilinx tools, specifically XSDB and
the associated JTAG device drivers. This also requires access to the JTAG interface
on the board, a number of AMD Xilinx and third-party boards come with on-board JTAG
modules.

### Sourcing the XSDB tools

Source the Vivado or Vitis tools `settings.sh` scripts.

### Deploying the images to target

Deploying the images can be done in two methods.

#### Using devtool boot-jtag script

1. Run devtool command to generate the boot-jtag.tcl script.
```
$ devtool boot-jtag --help
$ devtool boot-jtag --image core-image-minimal --hw_server TCP:<hostname/ip-addr>:3121
```
2. Script will be generated under ${DEPLOY_DIR_IMAGE}/boot-jtag.tcl
3. Execute this script using xsdb tool as shown below.
```
$ xsdb <absolute-path-to-deploy-dir-image>/boot-jtag.tcl
```

#### Manually executing xsdb commands

1. Power on the board, Launch the XSDB shell from command line as shown below.
```
$ xsdb
```
2. In the XSDB console, connect to the target over JTAG using the connect command.
   Optionally user can use `-url` to specify the local/remote hw_server. The 
   connect command returns the channel ID of the connection.
```
xsdb% connect
```
3. The targets command lists the available targets and allows you to select a
   target using its ID. The targets are assigned IDs as they are discovered on
   the JTAG chain, so the IDs can change from session to session.
```
xsdb% targets
```

> **Note:** For non-interactive usage such as scripting, you can use the `-filter`
   option to select a target instead of selecting the target using its ID.

##### Loading boot components using XSDB

1. Download the boot images for the target using XSDB with the `fpga` and `dow`
   command. ZyqnMP boot images will be located in the `${DEPLOY_DIR_IMAGE}`
   directory. 

> **Note:** In yocto by default, ${DEPLOY_DIR_IMAGE}/system.dtb is used for both
> u-boot and kernel.

2. Program the bitstream or skip this step if you are loading from u-boot or linux.
```
xsdb% fpga -no-revision-check ${DEPLOY_DIR_IMAGE}/download.bit
```
3. By default, JTAG security gates are enabled. Disable the security gates for 
   DAP, PL TAP, and PMU (this makes the PMU MB target visible to the debugger).
```
xsdb% targets -set -nocase -filter {name =~ "*PSU*"}
xsdb% mask_write 0xFFCA0038 0x1C0 0x1C0
```
3. Verify if the PMU MB target is listed under the PMU device. Now, load the PMU
   firmware.
```
xsdb% targets -set -nocase -filter {name =~ "*MicroBlaze PMU*"}
xsdb% catch {stop}
xsdb% dow ${DEPLOY_DIR_IMAGE}/pmufw.elf
xsdb% con
```
5. Reset APU Cortex-A53 Core 0 to load and execute FSBL, This step is important, 
   because when the ZynqMP boots up in JTAG boot mode, all the APU and RPU cores
   are held in reset. You must clear the resets on each core before performing 
   debugging on these cores. You can use the `rst` command in XSDB to clear the
   resets.
```
xsdb% targets -set -nocase -filter {name =~ "*A53*#0"}
xsdb% rst -processor -clear-registers
```
6. Download and run FSBL from APU Cortex-A53 Core 0
```
xsdb% dow ${DEPLOY_DIR_IMAGE}/zynqmp_fsbl.elf
xsdb% con
```
7. Now download TF-A, U-boot.elf and Device tree to APU and execute.
```
xsdb% stop
xsdb% dow ${DEPLOY_DIR_IMAGE}/bl31.elf
xsdb% dow ${DEPLOY_DIR_IMAGE}/u-boot.elf
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/system.dtb 0x100000
xsdb% con
```

8. In the target Serial Terminal, press any key to stop the U-Boot auto-boot.
```
...
Hit any key to stop autoboot: 0
U-Boot>
```

##### Loading Kernel, Root Filesystem and U-boot boot script

Load the images into the target DDR/PL DRR load address i.e.,
`DDR base address + <image_offset>`. 

Below example uses base DDR address as 0x0 which matches in vivado address editor.

| Image Type         | Base DDR Address | Image Offset | Load Address in DDR |
|--------------------|------------------|--------------|---------------------|
| Kernel             | 0x0              | 0x200000     | 0x200000            |
| Device Tree        | 0x0              | 0x1000       | 0x1000              |
| Rootfs             | 0x0              | 0x04000000   | 0x4000000           |
| U-boot boot script | 0x0              | 0x20000000   | 0x20000000          |

> **Note:** 
> 1. `<target-image>` refers to core-image-minimal or petalinux-image-minimal
> 2. For pxeboot boot create a symlink for `<target-image>-${MACHINE}-${DATETIME}.cpio.gz.u-boot`
> as shown `$ ln -sf ${DEPLOY_DIR_IMAGE}/<target-image>-${MACHINE}-${DATETIME}.cpio.gz.u-boot ${DEPLOY_DIR_IMAGE}/rootfs.cpio.gz.u-boot`
> to ensure the INITRD name in pxeboot.cfg matches with image name.
> 3. Whilst it is possible to load the images via JTAG this connection is slow and
this process can take a long time to execute (more than 10 minutes). If your
system has ethernet it is recommended that you use TFTP to load these images
using U-Boot. 
> 4. If common ${DEPLOY_DIR_IMAGE}/system.dtb is used by u-boot and kernel, this
> is already part of boot.bin we can skip loading dtb, else load kernel dtb.

###### Using XSDB

1. Suspend the execution of active target using `stop` command in XSDB.
```
xsdb% stop
```
2. Using the `dow` command to load the images into the target DDR/PL DDR load 
   address.
```
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/Image 0x200000
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/system.dtb 0x100000
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/core-image-minimal-${MACHINE}.cpio.gz.u-boot 0x4000000
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/boot.scr 0x20000000
```

###### Using TFTP

1. Setup TFTP directory on host machine and copy the images to your TFTP directory
   so that you can load them from U-Boot.
2. Configure the `ipaddr` and `serverip` of the U-Boot environment.
```
ZynqMP> set serverip <host-server-ip-address>
ZynqMP> set ipaddr <board-ip-address>
```
3. Load the images to DDR address.
```
U-Boot> tftpboot 0x200000 Image
U-Boot> tftpboot 0x100000 system.dtb
U-Boot> tftpboot 0x4000000 core-image-minimal-${MACHINE}.cpio.gz.u-boot
U-Boot> tftpboot 0x20000000 boot.scr

```
##### Booting Linux

Once the images are loaded continue the execution.

1. After loading images resume the execution of active target using the `con`
command in XSDB shell, Skip step 1 for if you have used TFTP to load images.
```
xsdb% con
```
2. Terminate xsdb shell.
```
xsdb% exit
```
3. In the target Serial Terminal, from U-Boot prompt run `boot` command.
```
U-Boot> boot
```

## Booting from SD

1. Load the SD card into the ZCU102 board in the J100 SD slot.
2. Configure the ZCU102 board to boot in SD-Boot mode (1-ON, 2-OFF, 3-OFF, 4-OFF)
   by setting the SW6. Refer [Setting Up the Target](#setting-up-the-target).
3. Follow SD boot instructions [README](README.booting.storage.md) for more details.

## Booting from QSPI

1. To boot ZCU012 board in QSPI boot mode, Power on the ZCU102 board and boot 
   using JTAG or SD boot mode, to ensure that U-Boot is running and also have 
   boot.bin copied to DDR location using XSDB `dow` or `tftpboot` or `fatload`
   command.
2. Follow Flash boot instructions [README](README.booting.flash.md) for more details.
3. After flashing the images, turn off the power switch on the board, and change
   the SW6 boot mode pin settings to QSPI boot mode (1-ON, 2-ON, 3-OFF, 4-ON) by
   setting the SW6. Refer [Setting Up the Target](#setting-up-the-target).
