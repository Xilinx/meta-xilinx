# Booting OS Images on MicroBlaze target boards

Booting OS images on MicroBlaze target boards can be done using JTAG and QSPI boot modes.

* [Setting Up the Target](#setting-up-the-target)
* [Booting from JTAG](#booting-from-jtag)
  * [Loading Bitstream using XSCT](#loading-bitstream-using-xsct)
  * [Loading U-boot using XSCT](#loading-u-boot-using-xsct)
  * [Loading Kernel, Device tree, Root Filesystem and U-boot boot script](#loading-kernel-device-tree-root-filesystem-and-u-boot-boot-script)
    * [Using XSCT](#using-xsct)
    * [Using TFTP](#using-tftp)

## Setting Up the Target

> **Note:** For microblaze-generic machine configuration file KCU105 evaluation 
> board is used as reference.

1. Connect a USB cable between the USB-JTAG, USB-UART connector on the target 
   and the USB port on the host machine. 
2. Connect 12V power to the KCU105 6-Pin power supply to J15 and turn on the board
   power with the SW1 switch.
3. Default UART terminal (serial port) settings is Speed `115200`, Data `8 bit`,
   Parity `None`, Stop bits ` 1 bit` and Flow control `None`.
4. Set the board to JTAG and other boot modes by setting the boot mode switch by 
   referring to board user guide. For KCU105 board below is the configuration 
   boot mode settings (SW15).

> **Note:** Switch OFF = 1 = High; ON = 0 = Low

| Boot Mode  | Mode Pins M[2:0] |
|------------|------------------|
| JTAG       | 101              |
| QSPI       | 001              |

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

### Loading Bitstream using XSCT

* Download the bitstream for the target using XSCT with the `fpga` command. Microblaze 
bitstream will be located in the `${DEPLOY_DIR_IMAGE}` directory. Optionally user
can use `fpga -no-revision-check` to skip FPGA silicon revision.

```
xsct% fpga -no-revision-check ${DEPLOY_DIR_IMAGE}/system-${MACHINE}.bit
xsct% after 2000
xsct% targets -set -nocase -filter {name =~ "microblaze*#0"}
xsct% catch {stop}
xsct% after 1000
```
### Loading U-boot using XSCT

1. Download `u-boot.elf` to the target CPU using XSCT. Microblaze u-boot.elf will be
located in the `${DEPLOY_DIR_IMAGE}` directory. Before u-boot.elf is loaded suspend
the execution of active target using `stop` command.
```
xsct% dow ${DEPLOY_DIR_IMAGE}/u-boot.elf
```
2. After loading u-boot.elf resume the execution of active target using the `con`
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

### Loading Kernel, Device tree, Root Filesystem and U-boot boot script

Load the images into the target DDR/MIG load address i.e., 
`DDR base address + <image_offset>`. MicroBlaze U-boot boot script(boot.scr) 
load address is calculated as `DDR base address + DDR Size - 0xe00000`

Below example uses base DDR address as 0x80000000 and DDR size as 0x80000000 
which matches in vivado address editor.

| Image Type         | Base DDR Address | Image Offset | Load Address in DDR |
|--------------------|------------------|--------------|---------------------|
| Kernel             | 0x80000000       | 0x0          | 0x80000000          |
| Device Tree        | 0x80000000       | 0x1e00000    | 0x81e00000          |
| Rootfs             | 0x80000000       | 0x2e00000    | 0x82e00000          |
| U-boot boot script | 0x80000000       | 0xe00000     | 0xff200000          |

> **Note:** 
> 1. `<target-image>` refers to core-image-minimal or petalinux-image-minimal
> 2. For pxeboot boot create a symlink for `<target-image>-${MACHINE}-${DATETIME}.cpio.gz.u-boot`
> as shown `$ ln -sf ${DEPLOY_DIR_IMAGE}/<target-image>-${MACHINE}-${DATETIME}.cpio.gz.u-boot ${DEPLOY_DIR_IMAGE}/rootfs.cpio.gz.u-boot`
> to ensure the INITRD name in pxeboot.cfg matches with image name.
> 3. Whilst it is possible to load the images via JTAG this connection is slow and
this process can take a long time to execute (more than 10 minutes). If your
system has ethernet it is recommended that you use TFTP to load these images
using U-Boot. 

#### Using XSCT

1. Suspend the execution of active target using `stop` command in XSCT.
```
xsct% stop
```
2. Using the `dow` command to load the images into the target DDR/MIG
load address.
```
xsct% dow -data ${DEPLOY_DIR_IMAGE}/linux.bin.ub 0x80000000
xsct% dow -data ${DEPLOY_DIR_IMAGE}/system.dtb 0x81e00000
xsct% dow -data ${DEPLOY_DIR_IMAGE}/core-image-minimal-${MACHINE}.cpio.gz.u-boot 0x82e00000
xsct% dow -data ${DEPLOY_DIR_IMAGE}/boot.scr 0xff200000
```

#### Using TFTP

1. Configure the `ipaddr` and `serverip` of the U-Boot environment. 
```
U-Boot> set serverip <server ip>
U-Boot> set ipaddr <board ip>
```
2. Load the images to DDR address. Make sure images are copied to tftp directory.
```
U-Boot> tftpboot 0x80000000 ${TFTPDIR}/linux.bin.ub
U-Boot> tftpboot 0x81e00000 ${TFTPDIR}/system.dtb
U-Boot> tftpboot 0x82e00000 ${TFTPDIR}/core-image-minimal-${MACHINE}.cpio.gz.u-boot
U-Boot> tftpboot 0xff200000 ${TFTPDIR}/boot.scr
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
