# JTAG boot script
#
################
@@PRE_BOOTENV@@

setenv kernel_bootcmd @@KERNEL_BOOTCMD@@

echo "Loading boot images from JTAG"
fdt addr ${fdtcontroladdr}
fdt get value bootargs /chosen bootargs
setenv bootargs $bootargs @@KERNEL_COMMAND_APPEND@@ @@KERNEL_ROOT_RAMDISK@@
${kernel_bootcmd} ${kernel_addr_r} ${ramdisk_addr_r} ${fdtcontroladdr}
