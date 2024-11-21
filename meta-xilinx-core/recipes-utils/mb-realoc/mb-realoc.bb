DESCRIPTION = "mb-realoc"

LICENSE = "CLOSED"

PROVIDES = "virtual/elfrealloc"

inherit deploy

SRC_URI:append = " file://mb-realoc"

PV = "0.1"

ELF_LOAD_ADDR ?= "0"
ELF_JUMP_OFFSET ?= ""
ELF_INFILE ?= "${DEPLOY_DIR_IMAGE}/u-boot.elf"
OUTFILE_NAME ?= "u-boot-s"
B = "${WORKDIR}"

PARALLEL_MAKE=""

do_configure[noexec]="1"
do_compile[depends] = "virtual/bootloader:do_deploy"

do_compile() {
	export CROSS_COMPILE="${TARGET_PREFIX}"
	${WORKDIR}/mb-realoc -l ${ELF_LOAD_ADDR} -i ${ELF_INFILE} -o ${OUTFILE_NAME}
}

do_install[noexec] = "1"

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0644 ${WORKDIR}/${OUTFILE_NAME}.bin ${DEPLOYDIR}/${OUTFILE_NAME}.bin
}

addtask deploy after do_compile
