
# Also deploy u-boot elf file with same file format as bin file
SRC_ELF ?= "u-boot"
DEST_ELF ?= "u-boot-${MACHINE}-${PV}-${PR}.elf"
ELF_SYMLINK ?= "u-boot-${MACHINE}.elf"
do_deploy_append() {
	install ${S}/${SRC_ELF} ${DEPLOYDIR}/${DEST_ELF}

	cd ${DEPLOYDIR}
	rm -f ${SRC_ELF} ${ELF_SYMLINK}
	ln -sf ${DEST_ELF} ${ELF_SYMLINK}
	ln -sf ${DEST_ELF} ${SRC_ELF}.elf
}

# Install the MicroBlaze System configuration into the board configuration
do_configure_prepend () {
	if [ "${SOC_FAMILY}" = "microblaze" ]; then
		if [ -e ${WORKDIR}/config.mk ]; then
			cp ${WORKDIR}/config.mk ${S}/board/xilinx/microblaze-generic/
		fi
		if [ -e ${WORKDIR}/xparameters.h ]; then
			cp ${WORKDIR}/xparameters.h ${S}/board/xilinx/microblaze-generic/
		fi
	fi
}
