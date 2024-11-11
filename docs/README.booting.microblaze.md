# Booting OS Images on MicroBlaze target boards

Booting OS images on MicroBlaze target boards can be done using JTAG and QSPI boot modes.

- [Booting OS Images on MicroBlaze target boards](#booting-os-images-on-microblaze-target-boards)
  - [Setting Up the Target](#setting-up-the-target)
  - [Booting from JTAG](#booting-from-jtag)
    - [Sourcing the XSDB tools](#sourcing-the-xsdb-tools)
    - [Deploying the images to target](#deploying-the-images-to-target)
      - [Using devtool boot-jtag script](#using-devtool-boot-jtag-script)
      - [Manually executing xsdb commands](#manually-executing-xsdb-commands)
        - [Loading Bitstream using XSDB](#loading-bitstream-using-xsdb)
        - [Loading U-boot using XSDB](#loading-u-boot-using-xsdb)
        - [Loading Kernel, Device tree, Root Filesystem and U-boot boot script](#loading-kernel-device-tree-root-filesystem-and-u-boot-boot-script)
          - [Using XSDB](#using-xsdb)
          - [Using TFTP](#using-tftp)
        - [Booting Linux](#booting-linux)
  - [Limitation](#limitation)

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

##### Loading Bitstream using XSDB

* Download the bitstream for the target using XSDB with the `fpga` command. Microblaze
bitstream will be located in the `${DEPLOY_DIR_IMAGE}` directory. Optionally user
can use `fpga -no-revision-check` to skip FPGA silicon revision.

```
xsdb% fpga -no-revision-check ${DEPLOY_DIR_IMAGE}/system-${MACHINE}.bit
xsdb% after 2000
xsdb% targets -set -nocase -filter {name =~ "microblaze*#0"}
xsdb% catch {stop}
xsdb% after 1000
```
##### Loading U-boot using XSDB

1. Download `u-boot.elf` to the target CPU using XSDB. Microblaze u-boot.elf will be
located in the `${DEPLOY_DIR_IMAGE}` directory. Before u-boot.elf is loaded suspend
the execution of active target using `stop` command.
```
xsdb% dow ${DEPLOY_DIR_IMAGE}/u-boot.elf
```
2. After loading u-boot.elf resume the execution of active target using the `con`
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

##### Loading Kernel, Device tree, Root Filesystem and U-boot boot script

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

###### Using XSDB

1. Suspend the execution of active target using `stop` command in XSDB.
```
xsdb% stop
```
2. Using the `dow` command to load the images into the target DDR/MIG
load address.
```
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/linux.bin.ub 0x80000000
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/system.dtb 0x81e00000
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/core-image-minimal-${MACHINE}.cpio.gz.u-boot 0x82e00000
xsdb% dow -data ${DEPLOY_DIR_IMAGE}/boot.scr 0xff200000
```

###### Using TFTP

1. Setup TFTP directory on host machine and copy the images to your TFTP directory
   so that you can load them from U-Boot.
2. Configure the `ipaddr` and `serverip` of the U-Boot environment.
```
U-Boot> set serverip <server ip>
U-Boot> set ipaddr <board ip>
```
3. Load the images to DDR address.
```
U-Boot> tftpboot 0x80000000 linux.bin.ub
U-Boot> tftpboot 0x81e00000 system.dtb
U-Boot> tftpboot 0x82e00000 core-image-minimal-${MACHINE}.cpio.gz.u-boot
U-Boot> tftpboot 0xff200000 boot.scr
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

## Limitation

1. Booting core-image-minimal or other image target excluding
   petalinux-image-minimal you can observe below error message.

```
Error: argument "/en*" is wrong: "dev" not a valid ifname
Starting syslogd/klogd: done

Poky (Yocto Project Reference Distro) 5.0.2 kcu105-microblazeel ttyUL0

INIT: Id "1" respawning too fast: disabled for 5 minutes

kcu105-microblazeel login:
```

This is due to pni-names distro feature is not enabled by default and eudev uses
classic network interface naming scheme. To resolve this issue add pni-names
distro feature from <distro>.conf or local.file.

```
DISTRO_FEATURES:append:microblaze = " pni-names"
```