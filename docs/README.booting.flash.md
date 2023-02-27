# Booting OS Images from Flash Device 

Booting OS Images from flash devices such as QSPI/NOR/NAND/OSPI.

* [U-boot boot scripts configurations](#u-boot-boot-scripts-configurations)
* [Booting from QSPI or NOR or OSPI](#booting-from-qspi-or-nor-or-ospi)

## U-boot boot scripts configurations

1. In QSPI/OSPI/NAND boot modes the boot.scr partition offset is fixed for all the
   platforms by default in u-boot, and you can change by updating 
   CONFIG_BOOT_SCRIPT_OFFSET in u-boot config. Default boot script size is 
   512KB(script_size_f=0x80000). 
2. Below table describes boot.scr partition offset and load address for all the
   platforms.

| Device     | Partition Offset address for boot.scr | Load address of boot.scr in DDR        |
|------------|---------------------------------------|----------------------------------------|
| MicroBlaze | 0x1F00000                             | DDR base address + DDR Size - 0xe00000 |
| Zynq-7000  | 0xFC0000                              | DDR base address + 0x3000000           |
| ZynqMP     | 0x3E80000                             | DDR base address + 0x20000000          |
| Versal     | 0x7F80000                             | DDR base address + 0x20000000          |

## Booting from QSPI or NOR or OSPI

This section demonstrates the booting OS images from QSPI boot mode. For this, 
you need to make sure you have QSPI interface on board or a QSPI daughter card.

> **Note:** Instructions are same for QSPI or NOR and OSPI flash.

1. For example we'll assume QSPI flash size is 128MB and default CONFIG_BOOT_SCRIPT_OFFSET
   defined in u-boot.

| Flash Partition Name | Partition Offset | Partition Size |
|----------------------|------------------|----------------|
| boot.bin             | 0x0              | 30MB           |
| bootenv              | 0x1E00000        | 256Kb          |
| kernel               | 0x1E40000        | 33MB           |
| bootscr              | 0x3E80000        | 1.5MB          |
| rootfs               | 0x4000000        | 64MB           |

2. Create a flash partition device-tree nodes depending on your flash size. ex:
```
&qspi {
	#address-cells = <1>;
	#size-cells = <0>;
	flash0: flash@0 {
		spi-tx-bus-width=<4>;
		spi-rx-bus-width=<4>;
		partition@0 {
			label = "boot";
			reg = <0x00000000 0x01e00000>;
		};
		partition@1 {
			label = "bootenv";
			reg = <0x01e00000 0x00040000>;
		};
		partition@2 {
			label = "kernel";
			reg = <0x01e40000 0x02040000>;
		};
		partition@3 {
			label = "bootscr";
			reg = <0x03e80000 0x01800000>;
		};
		partition@4 {
			label = "rootfs";
			reg = <0x04000000 0x04000000>;
		};
	};
};
```
3. Set the U-boot boot script variables to match the flash partition offsets in
   local.conf
```
QSPI_KERNEL_OFFSET = "0x1E40000"
QSPI_KERNEL_SIZE = "0x2040000"
QSPI_RAMDISK_OFFSET = "0x4000000"
QSPI_RAMDISK_SIZE = "0x4000000"
```
4. Build the images and make sure images are copied to tftp directory.
5. Once images are built, to ensure taget is booted using JTAG or SD boot modes.
6. Also have boot.bin copied to DDR location using XSCT `dow` or `tftpboot` or 
   `fatload` command.
7. Halt at U-Boot then run the following commands to flash the images on the
   QSPI flash.
```
# check QSPI is available or not
U-Boot> sf probe 0 0 0

# Erase the boot partition
U-Boot> sf erase 0x0 0x1E00000

# Copy the boot.bin to DDR location using tftpboot
U-Boot> tftpboot 0x10000000 ${TFTPDIR}/boot.bin

# Write boot.bin file image to flash partition
U-Boot> sf write 0x10000000 0x0 ${filesize}

# Erase the bootenv partition for env storage (saveenv).
U-Boot> sf erase 0x1E00000 0x1E40000

# Erase the kernel partition
U-Boot> sf erase 0x1E40000 0x2040000

# Copy the Image file to DDR location using tftpboot
U-Boot> tftpboot 0x10000000 ${TFTPDIR}/Image

# Write kernel image to flash partition
U-Boot> sf write 0x10000000 0x1E40000 ${filesize}

# Erase the bootscr partition
U-Boot> sf erase 0x3E80000 0x1800000

# Copy the boot.scr file to DDR location using tftpboot
U-Boot> tftpboot 0x10000000 ${TFTPDIR}/boot.scr

# Write boot.scr file to flash partition
U-Boot> sf write 0x10000000 0x3E80000 ${filesize}

# Erase the rootfs partition
U-Boot> sf erase 0x4000000 0x4000000

# Copy the rootfs.cpio.gz.u-boot file to DDR location using tftpboot
U-Boot> tftpboot 0x10000000 ${TFTPDIR}/rootfs.cpio.gz.u-boot

# Write rootfs image to flash partition
U-Boot> sf write 0x10000000 0x4000000 ${filesize}
```
8. After flashing the images, turn off the board and change the boot mode pin 
   settings to QSPI boot mode.
9. Power cycle the board. The board now boots up using the images in the QSPI
   flash.
