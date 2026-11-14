SUMMARY = "AMD MicroBlaze ELF relocator helper used to retarget u-boot \
to a new load address before deploy."
DESCRIPTION = "mb-realoc"

LICENSE = "LicenseRef-Xilinx-mb-realoc"
# mb-realoc only carries a copyright notice with no license grant;
# LICENSE.mb-realoc reproduces that notice
LIC_FILES_CHKSUM = "file://LICENSE.mb-realoc;md5=ab4b1d392e81f464a182412db967c84c \
                    file://mb-realoc;beginline=14;endline=15;md5=98529269b4dd365895052183a820d2fb"
NO_GENERIC_LICENSE[Xilinx-mb-realoc] = "LICENSE.mb-realoc"

PROVIDES = "virtual/elfrealloc"

inherit deploy

SRC_URI:append = " file://mb-realoc file://LICENSE.mb-realoc"

PV = "0.1"

ELF_LOAD_ADDR ?= "0"
ELF_JUMP_OFFSET ?= ""
ELF_INFILE ?= "${DEPLOY_DIR_IMAGE}/u-boot.elf"
OUTFILE_NAME ?= "u-boot-s"
S = "${UNPACKDIR}"

PARALLEL_MAKE = ""

do_configure[noexec] = "1"
do_compile[depends] = "virtual/bootloader:do_deploy"

do_compile() {
	install -d ${B}
	export CROSS_COMPILE="${TARGET_PREFIX}"
	${S}/mb-realoc -l ${ELF_LOAD_ADDR} -i ${ELF_INFILE} -o ${B}/${OUTFILE_NAME}
}

do_install[noexec] = "1"

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0644 ${B}/${OUTFILE_NAME}.bin ${DEPLOYDIR}/${OUTFILE_NAME}.bin
}

addtask deploy after do_compile
