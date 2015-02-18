
# Kernel version and SRCREV correspond to: xlnx_3.14 branch
LINUX_VERSION = "3.19"
KBRANCH ?= "master"
SRCREV ?= "bfa76d49576599a4b9f9b7a71f23d73d6dcff735"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-zynqmp-mainline:"
SRC_URI = " \
		git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git;protocol=https;branch=${KBRANCH} \
		file://xilinx-base;type=kmeta;destsuffix=xilinx-base \
		file://0001-ARM64-Add-new-Xilinx-ZynqMP-SoC.patch \
		file://0002-net-cadence-Enable-MACB-driver-for-ARM64.patch \
		file://0003-spi-Enable-Cadence-SPI-driver-for-ARM64.patch \
		file://0004-arm64-mm-Correct-check-for-EXEC-faults.patch \
		file://0005-arm64-Implement-cpu_relax-as-yield.patch \
		file://0006-arm64-spinlock-sev-when-unlocking-locks.patch \
		file://0007-kbuild-Create-directory-for-target-DTB.patch \
		"

COMPATIBLE_MACHINE_ep108-zynqmp = "ep108-zynqmp"
COMPATIBLE_MACHINE_qemuzynqmp = "qemuzynqmp"
KMACHINE_ep108-zynqmp ?= "zynqmp"
KMACHINE_qemuzynqmp ?= "zynqmp"

KERNEL_DEVICETREE_ep108-zynqmp = "xilinx/zynqmp-ep108.dtb"
KERNEL_DEVICETREE_qemuzynqmp = "xilinx/zynqmp-ep108.dtb"
