# Booting OS Images on Zynq target boards

Booting OS images on Zynq boards can be done using JTAG, SD, eMMC, QSPI and NAND
boot modes.

* [Setting Up the Target](#setting-up-the-target)

## Setting Up the Target
1. Connect a USB cable between the CP210x USB-to-UART bridge USB Mini-B on
   the target and the USB port on the host machine.
2. Connect a micro USB cable from the ZC702 board USB UART port (J17) to the USB 
   port on the host machine.
3. Default UART terminal(serial port) settings is Speed `115200`, Data `8 bit`,
   Parity `None`, Stop bits ` 1 bit` and Flow control `None`.
4. Set the board to JTAG and other boot mode by setting the boot mode switch by 
   referring to board user guide. For zynq-generic machine configuration
   file ZC702 evaluation board is used as reference and below is the
   configuration boot mode settings (SW16).

> **Note:** Switch OFF = 0 = Low; ON = 1 = High

| Boot Mode | Mode Pins [0:4] | Mode SW16 [1:5]         | Comments               |
|-----------|-----------------|-------------------------|------------------------|
| JTAG      | 00000           | OFF, OFF, OFF, OFF, OFF | PS JTAG                |
| QSPI      | 01000           | OFF, ON, OFF, OFF, OFF  | QSPI 32-bit addressing |
| SD        | 00110           | OFF, OFF, ON, ON, OFF   | SD 2.0                 |

## Booting from JTAG

This boot flow requires the use of the AMD Xilinx tools, specifically XSCT and 
the associated JTAG device drivers. This also requires access to the JTAG interface
on the board, a number of AMD Xilinx and third-party boards come with on-board JTAG
modules.

1. Source the Vivado or Vitis tools `settings.sh` scripts.
2. Power on the board, Open the XSCT console in the Vitis IDE by clicking the
   XSCT button. Alternatively, you can also open the XSCT console by selecting
   Xilinx -> XSCT Console.
```
$ xsct
```
3. In the XSCT console, connect to the target over JTAG using the connect command.
   Optionally user can use `-url` to specify the local/remote hw_server. The 
   connect command returns the channel ID of the connection.
```
xsct% connect
```
4. The targets command lists the available targets and allows you to select a
   target using its ID. The targets are assigned IDs as they are discovered on
   the JTAG chain, so the IDs can change from session to session.
```
xsct% targets
```

> **Note:** For non-interactive usage such as scripting, you can use the `-filter`
   option to select a target instead of selecting the target using its ID.

### Loading boot components using XSCT

1. Download the boot images for the target using XSCT with the `fpga` and `dow` 
   command. Zynq boot images will be located in the `${DEPLOY_DIR_IMAGE}`
   directory. 

> **Note:** In yocto by default, ${DEPLOY_DIR_IMAGE}/system.dtb is used for both
> u-boot and kernel.

2. Program the bitstream or skip this step if you are loading from u-boot or linux.
```
xsct% fpga -no-revision-check ${DEPLOY_DIR_IMAGE}/download.bit
```
3. Select APU Cortex-A9 Core 0 to load and execute FSBL.
```
xsct% targets -set -nocase -filter {name =~ "arm*#0"}
xsct% catch {stop}
```
5. Download and run FSBL from APU Cortex-A9 Core 0
```
xsct% dow ${DEPLOY_DIR_IMAGE}/zynq_fsbl.elf
xsct% con
```
7. Now download U-boot.elf and Device tree to APU and execute.
```
xsct% stop
xsct% dow ${DEPLOY_DIR_IMAGE}/u-boot.elf
xsct% dow -data ${DEPLOY_DIR_IMAGE}/system.dtb 0x100000
xsct% con
```

8. In the target Serial Terminal, press any key to stop the U-Boot auto-boot.
```
...
Hit any key to stop autoboot: 0
U-Boot>
```

### Loading Kernel, Root Filesystem and U-boot boot script

Load the images into the target DDR load address i.e.,
`DDR base address + <image_offset>`. 

Below example uses base DDR address as 0x0 which matches in vivado address editor.

| Image Type         | Base DDR Address | Image Offset  | Load Address in DDR |
|--------------------|------------------|---------------|---------------------|
| Kernel             | 0x0              | 0x200000      | 0x200000            |
| Device Tree        | 0x0              | 0x100000      | 0x100000            |
| Rootfs             | 0x0              | 0x4000000     | 0x4000000           |
| U-boot boot script | 0x0              | 0x3000000     | 0x3000000           |

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

#### Using XSCT

1. Suspend the execution of active target using `stop` command in XSCT.
```
xsct% stop
```
2. Using the `dow` command to load the images into the target DDR/PL DDR load 
   address.
```
xsct% dow -data ${DEPLOY_DIR_IMAGE}/uImage 0x200000
xsct% dow -data ${DEPLOY_DIR_IMAGE}/system.dtb 0x100000
xsct% dow -data ${DEPLOY_DIR_IMAGE}/core-image-minimal-${MACHINE}.cpio.gz.u-boot 0x4000000
xsct% dow -data ${DEPLOY_DIR_IMAGE}/boot.scr 0x3000000
```

#### Using TFTP

1. Configure the `ipaddr` and `serverip` of the U-Boot environment. 
```
Versal> set serverip <server ip>
Versal> set ipaddr <board ip>
```
2. Load the images to DDR address. Make sure images are copied to tftp directory.
```
U-Boot> tftpboot 0x200000 ${TFTPDIR}/uImage
U-Boot> tftpboot 0x100000 ${TFTPDIR}/system.dtb
U-Boot> tftpboot 0x4000000 ${TFTPDIR}/core-image-minimal-${MACHINE}.cpio.gz.u-boot
U-Boot> tftpboot 0x3000000 ${TFTPDIR}/boot.scr

```
### Booting Linux

Once the images are loaded continue the execution.

1. After loading images resume the execution of active target using the `con`
command in XSCT shell, Skip step 1 for if you have used TFTP to load images.
```
xsct% con
```
2. Terminate xsct shell.
```
xsct% exit
```
3. In the target Serial Terminal, from U-Boot prompt run `boot` command.
```
U-Boot> boot
```

## Booting from SD

1. Load the SD card into the ZC702 board in the SD slot.
2. Configure the ZC702 board to boot in SD-Boot mode (1-OFF, 2-OFF, 3-ON, 4-ON, 5-OFF)
   by setting the SW6. Refer [Setting Up the Target](#setting-up-the-target).
3. Follow SD boot instructions [README](README.booting.storage.md) for more details.

## Booting from QSPI

1. To boot ZC702 board in QSPI boot mode, Power on the ZCU102 board and boot 
   using JTAG or SD boot mode, to ensure that U-Boot is running and also have 
   boot.bin copied to DDR location using XSCT `dow` or `tftpboot` or `fatload`
   command.
2. Follow Flash boot instructions [README](README.booting.flash.md) for more details.
3. After flashing the images, turn off the power switch on the board, and change
   the SW16 boot mode pin settings to QSPI boot mode (1-OFF, 2-ON, 3-OFF, 4-OFF, 5-OFF)
   by setting the SW16. Refer [Setting Up the Target](#setting-up-the-target).