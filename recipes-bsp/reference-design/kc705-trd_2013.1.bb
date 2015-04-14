SUMMARY = "KC705 Targeted Reference Design"
DESCRIPTION = "Contains the Reference Design Files and pre-built bitstream."
HOMEPAGE = "http://www.xilinx.com"
SECTION = "bsp"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://readme.txt;md5=3460f0b771d39ff306837a28cd1d2532"

COMPATIBLE_MACHINE = "kc705-trd-microblazeel"

SRC_URI = "http://www.xilinx.com/support/documentation/boards_and_kits/k7_emb/2013_1/k7-embedded-trd-rdf0283.zip"
SRC_URI[md5sum] = "226cac219b1307cd465caa411d76d657"
SRC_URI[sha256sum] = "82096948c2c74a16f4d6c5a43d9e6d76eab73e322ace56d30f9c84a9be43edbe"

S = "${WORKDIR}/k7-embedded-trd-rdf0283"

PROVIDES = "virtual/bitstream"

FILES_${PN} += "/boot/download.bit"

INHIBIT_DEFAULT_DEPS = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Copy the bitstream into the boot directory
do_install() {
	install -d ${D}/boot
	install ${S}/KC705_Embedded_Kit/KC705_System/ready_for_download/download.bit ${D}/boot/download.bit
}

do_compile() {
	:
}

do_deploy () {
	install -d ${DEPLOY_DIR_IMAGE}
	if [ -e ${D}/boot/download.bit ]; then
		install ${D}/boot/download.bit ${DEPLOY_DIR_IMAGE}/download.bit
	fi
}

addtask deploy before do_build after do_install

