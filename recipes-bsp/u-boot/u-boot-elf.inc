# U-Boot deploy ELF
UBOOT_ELF = "u-boot"
UBOOT_ELF_aarch64 = "u-boot.elf"
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
