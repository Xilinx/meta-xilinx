# Boot script for U-Boot - default SD/eMMC distro boot flow for
# u-boot-xlnx-scr. Works with any block device (MMC, UFS/SCSI, USB) where
# devtype and devnum are set by the distro boot framework before this
# script runs.
#
#
################

setenv kernel_name @@KERNEL_IMAGE@@
setenv rootpartnum @@ROOT_PARTNUM@@
setenv kernel_bootcmd @@KERNEL_BOOTCMD@@

if test -n "@@BITSTREAM@@"; then
	echo "Loading Bitstream from U-boot"
	fatload  ${devtype} ${devnum} @@BITSTREAM_LOAD_ADDRESS@@ @@BITSTREAM_IMAGE@@
	fpga @@BITSTREAM_LOAD_TYPE@@ 0 @@BITSTREAM_LOAD_ADDRESS@@ ${filesize}
fi

echo "Checking for kernel: /${kernel_name}"
if test -e ${devtype} ${devnum}:${distro_bootpart} /${kernel_name}; then
	echo "Loading ${kernel_name} at ${kernel_addr_r}"
	fatload ${devtype} ${devnum}:${distro_bootpart} ${kernel_addr_r} ${kernel_name};
else
	echo "kernel image /boot/${kernel_name} not found on ${devtype} ${devnum}:${distro_bootpart}"
	exit
fi

part uuid ${devtype} ${devnum}:${rootpartnum} distro_rootpart_uuid
if test -z "${distro_rootpart_uuid}"; then
	echo "Failed to get PARTUUID for ${devtype} ${devnum}:${rootpartnum}"
	exit
fi

fdt addr ${fdtcontroladdr}
fdt get value bootargs /chosen bootargs
setenv bootargs ${bootargs} root=PARTUUID=${distro_rootpart_uuid} ro rootwait
${kernel_bootcmd} ${kernel_addr_r} - ${fdtcontroladdr}
