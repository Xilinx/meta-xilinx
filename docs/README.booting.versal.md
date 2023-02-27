# Booting OS Images on Versal target boards

Booting OS images on Versal boards can be done using JTAG, SD, eMMC and QSPI boot
modes.

* [Setting Up the Target](#setting-up-the-target)
* [Booting from JTAG](#booting-from-jtag)
  * [Loading boot.bin using XSCT](#loading-bootbin-using-xsct)
  * [Loading Kernel, Root Filesystem and U-boot boot script](#loading-kernel-root-filesystem-and-u-boot-boot-script)
    * [Using XSCT](#using-xsct)
    * [Using TFTP](#using-tftp)
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

### Loading boot.bin using XSCT

1. Download the boot.bin for the target using XSCT with the `device program` command.
Versal boot.bin will be located in the `${DEPLOY_DIR_IMAGE}` directory. Default
boot.bin consists of boot pdi, plm.elf, psm.elf, bl31.elf, u-boot.elf and 
system.dtb. This boot.bin is generated using bootgen tool by passing a .bif file.

> **Note:** In yocto by default, ${DEPLOY_DIR_IMAGE}/system.dtb is used for both
> u-boot and kernel.

```
xsct% targets -set -nocase -filter {name =~ "*PMC*"}
xsct% device program ${DEPLOY_DIR_IMAGE}/boot.bin
xsct% targets -set -nocase -filter {name =~ "*A72*#0"}
xsct% stop
```
2. After loading boot.bin resume the execution of active target using the `con`
command in XSCT shell.
```
xsct% con
```
3. In the target Serial Terminal, press any key to stop the U-Boot auto-boot.
```
...
Hit any key to stop autoboot: 0
U-Boot>
```

### Loading Kernel, Root Filesystem and U-boot boot script

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

#### Using XSCT

1. Suspend the execution of active target using `stop` command in XSCT.
```
xsct% stop
```
2. Using the `dow` command to load the images into the target DDR/PL DDR load 
   address.
```
xsct% dow -data ${DEPLOY_DIR_IMAGE}/Image 0x200000
xsct% dow -data ${DEPLOY_DIR_IMAGE}/system.dtb 0x1000
xsct% dow -data ${DEPLOY_DIR_IMAGE}/core-image-minimal-${MACHINE}.cpio.gz.u-boot 0x4000000
xsct% dow -data ${DEPLOY_DIR_IMAGE}/boot.scr 0x20000000
xsct% targets -set -nocase -filter {name =~ "*A72*#0"}
```

#### Using TFTP

1. Configure the `ipaddr` and `serverip` of the U-Boot environment. 
```
Versal> set serverip <server ip>
Versal> set ipaddr <board ip>
```
2. Load the images to DDR address. Make sure images are copied to tftp directory.
```
U-Boot> tftpboot 0x200000 ${TFTPDIR}/Image
U-Boot> tftpboot 0x1000 ${TFTPDIR}/system.dtb
U-Boot> tftpboot 0x4000000 ${TFTPDIR}/core-image-minimal-${MACHINE}.cpio.gz.u-boot
U-Boot> tftpboot 0x20000000 ${TFTPDIR}/boot.scr

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

1. Load the SD card into the VCK190 board in the J302 SD slot.
2. Configure the VCK190 board to boot in SD-Boot mode (1-ON, 2-OFF, 3-OFF, 4-OFF)
   by setting the SW1. Refer [Setting Up the Target](#setting-up-the-target).
3. Follow SD boot instructions [README](README.booting.storage.md) for more details.

## Booting from QSPI

1. To boot VCK190 board in QSPI boot mode, you need to connect a QSPI daughter
   card (part number: X_EBM-01, REV_A01).
2. With the card powered off, install the QSPI daughter card.
3. Power on the VCK190 board and boot using JTAG or SD boot mode, to ensure that
   U-Boot is running and also have boot.bin copied to DDR location using XSCT
   `dow` or `tftpboot` or `fatload` command.
4. Follow Flash boot instructions [README](README.booting.flash.md) for more details.
5. After flashing the images, turn off the power switch on the board, and change
   the SW1 boot mode pin settings to QSPI boot mode (1-ON, 2-OFF, 3-ON, 4-ON) by
   setting the SW1. Refer [Setting Up the Target](#setting-up-the-target).