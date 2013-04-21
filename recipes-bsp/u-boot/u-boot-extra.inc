
# Move into include?
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