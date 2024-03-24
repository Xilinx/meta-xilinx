# Booting OS Images from Storage Device 

Booting OS Images from storage devices such as SD Card, eMMC, USB and SATA devices.

* [Booting from SD or eMMC](#booting-from-sd-or-emmc)
* [Writing wic image to SD or eMMC device](#writing-image-to-sd-or-emmc-device)
  * [Using Wic file](#using-wic-file)
  * [Using Yocto images](#using-yocto-images)
* [Secondary boot from USB or SATA device](#secondary-boot-from-usb-or-sata-device)

## Booting from SD or eMMC

Setup the card with the first partition formatted as FAT16/32. If you intend to
boot with the root filesystem located on the SD card, also create a second
partition formatted as EXT4.

It is recommended that the first partition be at least 512MB in size, however
this value will depend on whether using a ramdisk for the root filesystem and
how large the ramdisk is.

This section describes how to manually prepare and populate an SD card image.
There are automation tools in OpenEmbedded that can generate disk images already
formatted and prepared such that they can be written directly to a disk. Refer
to the Yocto Project Manual for more details:
https://docs.yoctoproject.org/4.1.4/singleindex.html#creating-partitioned-images-using-wic

## Writing image to SD or eMMC device

There are two ways to write the images to SD card or eMMC device.

1. Find the device name of SD or eMMC device and make sure it is unmounted. In
   this example we'll assume it is /dev/mmcblk<devnum><partnum>.
2. To write image to eMMC device make sure you need to boot Linux from JTAG or 
   SD or QSPI first, then copy the wic image to `<target_rootfs>/tmp` directory.

> **Note:** `<target-image>` refers to core-image-minimal or petalinux-image-minimal

### Using Wic file

Write wic image file to the SD card or eMMC device. Use dd command or balena
etcher to flash the wic image file to SD card. WIC image will be
build/tmp/deploy/${MACHINE}/<target-image>-${MACHINE}-${DATETIME}.rootfs.wic, See
[Flashing Images Using bmaptool](https://docs.yoctoproject.org/singleindex.html#flashing-images-using-bmaptool)
for fast and easy way to flash the image
```
$ sudo dd if=<target-image>-${MACHINE}-${DATETIME}.rootfs.wic of=/dev/mmcblk<devnum> bs=4M
```

### Using Yocto images

> **Note:** Use actual files to copy and don't use symlink files.

1. Create a FAT32 and EXT4 partition on SD card or eMMC device.
```
$ sudo parted -s /dev/mmcblk<devnum> mklabel gpt mkpart primary fat32 1MiB 512MiB mkpart ext4 512MiB 8GiB name 1 boot name 2 root
$ sudo mkfs.fat -n boot /dev/mmcblk<devnum>1 && sudo mkfs.ext4 -L root /dev/mmcblk<devnum>2
$ sudo lsblk /dev/mmcblk<devnum> -o NAME,FSTYPE,LABEL,PARTLABEL
```
2. Mount the FAT32 and EXT4 partition.
```
$ sudo mount -L boot /mnt/boot; sudo mount -L root /mnt/rootfs` 
```
3. Copy the boot images to the SD card or eMMC device FAT32 partition.

* Linux
   * boot.bin
   * boot.scr
   * Image or uImage (For Zynq7000 only)
   * system.dtb
   * rootfs.cpio.gz.u-boot (If using a ramdisk)
   ```
   $ cp ${DEPLOY_DIR_IMAGE}/boot.bin /mnt/boot/boot.bin
   $ cp ${DEPLOY_DIR_IMAGE}/boot.scr /mnt/boot/boot.scr
   $ cp ${DEPLOY_DIR_IMAGE}/Image /mnt/boot/Image
   $ cp ${DEPLOY_DIR_IMAGE}/system.dtb /mnt/boot/system.dtb
   $ cp ${DEPLOY_DIR_IMAGE}/<target-image>-${MACHINE}-${DATETIME}.cpio.gz.u-boot /mnt/boot/rootfs.cpio.gz.u-boot
   ```
* Xen
   * boot.bin
   * boot.scr
   * Image
   * xen
   * system.dtb
   * rootfs.cpio.gz (If using a ramdisk)
   ```
   $ cp ${DEPLOY_DIR_IMAGE}/boot.bin /mnt/boot/boot.bin
   $ cp ${DEPLOY_DIR_IMAGE}/boot.scr /mnt/boot/boot.scr
   $ cp ${DEPLOY_DIR_IMAGE}/Image /mnt/boot/Image
   $ cp ${DEPLOY_DIR_IMAGE}/xen /mnt/boot/xen
   $ cp ${DEPLOY_DIR_IMAGE}/system.dtb /mnt/boot/system.dtb
   $ cp ${DEPLOY_DIR_IMAGE}/<target-image>-${MACHINE}-${DATETIME}.cpio.gz /mnt/boot/rootfs.cpio.gz
   ```

4. Extract `<target-image>-${MACHINE}-${DATETIME}.rootfs.tar.gz` file content to the SD
   card or eMMC device EXT4 partition.
```
$ sudo tar -xf ${DEPLOY_DIR_IMAGE}/<target-image>-${MACHINE}-${DATETIME}.rootfs.tar.gz -C /mnt/rootfs
$ sync
```
5. Unmount the SD Card or eMMC device and boot from SD or eMMC boot modes.
```
$ umount /mnt/boot
$ umount /mnt/rootfs
```

## Secondary boot from USB or SATA device

On Zynq, ZynqMP and Versal devices supports secondary boot medium such as USB or
SATA external storage devices. This means target soc primary boot medium should
be either JATG or SD/eMMC or QSPI/NOR/NAND boot modes.

> **Note:** Use actual files to copy and don't use symlink files.

1. Create a FAT32 and EXT4 partition on SD card or eMMC device.
```
$ sudo parted -s /dev/sd<X> mklabel gpt mkpart primary mkpart ext4 512MiB 8GiB name 1 root
$ sudo sudo mkfs.ext4 -L root /dev/sd<X>1
$ sudo lsblk /dev/sd<X> -o NAME,FSTYPE,LABEL,PARTLABEL
```
2. Mount the FAT32 and EXT4 partition.
```
$ sudo mount -L root /mnt/rootfs` 
```
3. Extract `<target-image>-${MACHINE}-${DATETIME}.rootfs.tar.gz` file content
   to the USB or SATA device EXT4 partition.
```
$ sudo tar -xf ${DEPLOY_DIR_IMAGE}/<target-image>-${MACHINE}-${DATETIME}.rootfs.tar.gz -C /mnt/rootfs
$ sync
```
4. Unmount the USB or SATA device.
```
$ umount /mnt/rootfs
```
5. Boot from JATG or SD/eMMC or QSPI/NOR/NAND boot modes and halt at u-boot.
6. Set U-boot bootargs for USB or SATA rootfs and boot from run secondary boot 
   from USB or SATA device
```
U-Boot> setenv sata_root 'setenv bootargs ${bootargs} root=/dev/sd<X>1 rw rootfstype=ext4 rootwait'
U-Boot> setenv sataboot 'run sata_root; run default_bootcmd'
U-Boot> saveenv
U-Boot> run sataboot
```
