LINUX_VERSION = "4.18"
XILINX_RELEASE_VERSION = "versal"
KBRANCH ?= "xilinx/versal"
SRCREV ?= "29a4de49ceed0efccc226e95c4448f4f41755e7a"

include linux-xlnx.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

COMPATIBLE_MACHINE_versal = ".*"

KBUILD_DEFCONFIG_versal = "xilinx_versal_defconfig"

KERNEL_FEATURES_versal += "${@bb.utils.contains('DISTRO_FEATURES', 'xen', ' bsp/xilinx/xen.scc', '', d)}"
