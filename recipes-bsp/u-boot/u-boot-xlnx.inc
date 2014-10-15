require recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"

XILINX_EXTENSION = "-xilinx"

SRC_URI = "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https"

S = "${WORKDIR}/git"
PACKAGE_ARCH = "${MACHINE_ARCH}"

include u-boot-extra.inc

SPL_BINARY_zynq = "boot.bin"
UBOOT_SUFFIX = "img"

do_configure_prepend () {
	if test -e ${WORKDIR}/ps7_init.h; then
		cp ${WORKDIR}/ps7_init.h ${S}/board/xilinx/zynq/
		cp ${WORKDIR}/ps7_init.c ${S}/board/xilinx/zynq/
	fi
}

# U-Boot deploy ELF
UBOOT_ELF = "u-boot"
UBOOT_ELF_IMAGE ?= "u-boot-${MACHINE}-${PV}-${PR}.elf"
UBOOT_ELF_BINARY ?= "u-boot.elf"
UBOOT_ELF_SYMLINK ?= "u-boot-${MACHINE}.elf"

do_deploy_append() {
	if [ ! -z "${UBOOT_ELF}" ]; then
		install ${S}/${UBOOT_ELF} ${DEPLOYDIR}/${UBOOT_ELF_IMAGE}
		rm -f ${DEPLOYDIR}/${UBOOT_ELF_BINARY} ${DEPLOYDIR}/${UBOOT_ELF_SYMLINK}
		ln -sf ${UBOOT_ELF_IMAGE} ${DEPLOYDIR}/${UBOOT_ELF_BINARY}
		ln -sf ${UBOOT_ELF_IMAGE} ${DEPLOYDIR}/${UBOOT_ELF_SYMLINK}
	fi
}

