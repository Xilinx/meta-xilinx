# NAND Flash boot script
# Loads the kernel and rootfs (ramdisk) from their respective NAND flash
# partitions (NAND_KERNEL_OFFSET/SIZE and NAND_RAMDISK_OFFSET/SIZE) into
# DDR, then boots the kernel with the ramdisk as the root filesystem.
#
################
@@PRE_BOOTENV@@

setenv kernel_bootcmd @@KERNEL_BOOTCMD@@

setenv kernel_addr_r @@KERNEL_LOAD_ADDRESS@@
setenv nand_kernel_offset @@NAND_KERNEL_OFFSET@@
setenv nand_kernel_size @@NAND_KERNEL_SIZE@@
setenv ramdisk_addr_r @@RAMDISK_IMAGE_ADDRESS@@
setenv nand_ramdisk_offset @@NAND_RAMDISK_OFFSET@@
setenv nand_ramdisk_size @@NAND_RAMDISK_SIZE@@

echo "Loading boot images from NAND flash"
nand info
nand read ${kernel_addr_r} ${nand_kernel_offset} ${nand_kernel_size}
nand read ${ramdisk_addr_r} ${nand_ramdisk_offset} ${nand_ramdisk_size}
fdt addr ${fdtcontroladdr}
fdt get value bootargs /chosen bootargs
setenv bootargs $bootargs @@KERNEL_COMMAND_APPEND@@ @@KERNEL_ROOT_RAMDISK@@
${kernel_bootcmd} ${kernel_addr_r} ${ramdisk_addr_r} ${fdtcontroladdr}
