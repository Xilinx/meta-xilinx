# Booting OS Images on Versal target boards

Booting OS images on Versal boards can be done using JTAG, SD, eMMC and QSPI boot
modes.

* [Setting Up the Target](#setting-up-the-target)
* [Booting from JTAG](#booting-from-jtag)
  * [Sourcing the XSDB tools](#sourcing-the-xsdb-tools)
  * [Deploying the images to target](#deploying-the-images-to-target)
    * [Using devtool boot-jtag script](#using-devtool-boot-jtag-script)
    * [Manually executing xsdb commands](#manually-executing-xsdb-commands)
      * [Loading boot.bin using XSDB](#loading-bootbin-using-xsdb)
      * [Loading Kernel, Root Filesystem and U-boot boot script](#loading-kernel-root-filesystem-and-u-boot-boot-script)
        * [Using XSDB](#using-xsdb)
        * [Using TFTP](#using-tftp)
      * [Booting Linux](#booting-linux)
* [Booting from SD](#booting-from-sd)
* [Booting from QSPI](#booting-from-qspi)

## Setting Up the Target

> **Note:** For versal-generic machine configuration file VCK190 evaluation 
> board is used as reference.

1. Connect a USB cable between the FTDI FT4232HL U20 USB-to-Quad-UART bridge USB
   Type-C connector on the target and the USB port on the host machine.
2. Connect 12V power to the VCK190 6-Pin Molex connector and turn on the board
   power with the SW13 switch.
3. Default UART terminal (serial port) settings is Speed `115200`, Data `8 bit`,
   Parity `None`, Stop bits ` 1 bit` and Flow control `None`.
4. Set the board to JTAG and other boot modes by setting the boot mode switch by 
   referring to board user guide. For VCK190 board Below is the configuration 
   boot mode settings (SW1).

> **Note:** Switch OFF = 1 = High; ON = 0 = Low

| Boot Mode | Mode Pins [3:0] | Mode SW1 [4:1]    | Comments                                          |
|-----------|-----------------|-------------------|---------------------------------------------------|
| JTAG      | 0000            | ON, ON, ON, ON    | Supported with or without boot module attached    |
| QSPI      | 0010            | ON, ON, OFF, ON   | Supported only with boot module X-EBM-01 attached |
| SD        | 1110            | OFF, OFF, OFF, ON | Supported with or without boot module attached    |

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

##### Loading boot.bin using XSDB

1. Download the boot.bin for the target using XSDB with the `device program` command.
Versal boot.bin will be located in the `${DEPLOY_DIR_IMAGE}` directory. Default
boot.bin consists of boot pdi, plm.elf, psm.elf, bl31.elf, u-boot.elf and 
system.dtb. This boot.bin is generated using bootgen tool by passing a .bif file.

> **Note:** In yocto by default, ${DEPLOY_DIR_IMAGE}/system.dtb is used for both
> u-boot and kernel.

```
xsdb% targets -set -nocase -filter {name =~ "*PMC*"}
xsdb% device program ${DEPLOY_DIR_IMAGE}/boot.bin
xsdb% targets -set -nocase -filter {name =~ "*A72*#0"}
xsdb% stop
```
2. After loading boot.bin resume the execution of active target using the `con`
command in XSDB shell.
```
xsdb% con
```
3. In the target Serial Terminal, press any key to stop the U-Boot auto-boot.
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
| Rootfs             | 0x0              | 0x4000000    | 0x4000000           |
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
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/system.dtb 0x1000
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/core-image-minimal-${MACHINE}.cpio.gz.u-boot 0x4000000
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/boot.scr 0x20000000
xsdb% targets -set -nocase -filter {name =~ "*A72*#0"}
```

###### Using TFTP

1. Setup TFTP directory on host machine and copy the images to your TFTP directory
   so that you can load them from U-Boot.
2. Configure the `ipaddr` and `serverip` of the U-Boot environment.
```
Versal> set serverip <server ip>
Versal> set ipaddr <board ip>
```
3. Load the images to DDR address.
```
U-Boot> tftpboot 0x200000 Image
U-Boot> tftpboot 0x1000 system.dtb
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

1. Load the SD card into the VCK190 board in the J302 SD slot.
2. Configure the VCK190 board to boot in SD-Boot mode (1-ON, 2-OFF, 3-OFF, 4-OFF)
   by setting the SW1. Refer [Setting Up the Target](#setting-up-the-target).
3. Follow SD boot instructions [README](README.booting.storage.md) for more details.

## Booting from QSPI

1. To boot VCK190 board in QSPI boot mode, you need to connect a QSPI daughter
   card (part number: X_EBM-01, REV_A01).
2. With the card powered off, install the QSPI daughter card.
3. Power on the VCK190 board and boot using JTAG or SD boot mode, to ensure that
   U-Boot is running and also have boot.bin copied to DDR location using XSDB
   `dow` or `tftpboot` or `fatload` command.
4. Follow Flash boot instructions [README](README.booting.flash.md) for more details.
5. After flashing the images, turn off the power switch on the board, and change
   the SW1 boot mode pin settings to QSPI boot mode (1-ON, 2-OFF, 3-ON, 4-ON) by
   setting the SW1. Refer [Setting Up the Target](#setting-up-the-target).
