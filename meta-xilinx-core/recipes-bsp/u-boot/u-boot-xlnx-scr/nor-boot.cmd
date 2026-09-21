# NOR Flash boot script
# Loads the kernel and rootfs (ramdisk) from their respective NOR flash
# partitions (SPI_KERNEL_OFFSET/SIZE and SPI_RAMDISK_OFFSET/SIZE) into
# DDR, then boots the kernel with the ramdisk as the root filesystem.
#
################
@@PRE_BOOTENV@@

setenv kernel_bootcmd @@KERNEL_BOOTCMD@@

setenv kernel_addr_r @@KERNEL_LOAD_ADDRESS@@
setenv spi_kernel_offset @@SPI_KERNEL_OFFSET@@
setenv spi_kernel_size @@SPI_KERNEL_SIZE@@
setenv ramdisk_addr_r @@RAMDISK_IMAGE_ADDRESS@@
setenv spi_ramdisk_offset @@SPI_RAMDISK_OFFSET@@
setenv spi_ramdisk_size @@SPI_RAMDISK_SIZE@@

echo "Loading boot images from NOR flash"
cp.b ${kernel_addr_r} ${spi_kernel_offset} ${spi_kernel_size}
cp.b ${ramdisk_addr_r} ${spi_ramdisk_offset} ${spi_ramdisk_size}
fdt addr ${fdtcontroladdr}
fdt get value bootargs /chosen bootargs
setenv bootargs $bootargs @@KERNEL_COMMAND_APPEND@@ @@KERNEL_ROOT_RAMDISK@@
${kernel_bootcmd} ${kernel_addr_r} ${ramdisk_addr_r} ${fdtcontroladdr}
