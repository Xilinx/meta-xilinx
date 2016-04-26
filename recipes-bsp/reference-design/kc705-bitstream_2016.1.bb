SUMMARY = "KC705 design file for bitstream"
DESCRIPTION = "Contains the pre-built bitstream and hardware project."
HOMEPAGE = "http://www.xilinx.com"
SECTION = "bsp"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://implementation/system.mmi;md5=1992d9c9006c524f4178949897749f21"

COMPATIBLE_MACHINE = "kc705-microblazeel"

SRC_URI = "http://www.xilinx.com/support/documentation/boards_and_kits/k7_emb/2016_1/xilinx-kc705-axi-full-2016.1.tar"
SRC_URI[md5sum] = "59ade57ab0a15c8700c129f040fe4c75"
SRC_URI[sha256sum] = "8e3e5c5d30e6d02eaa58cb6e9255b8d6456857185744904fb931e3d8e44ba62e"

S = "${WORKDIR}/Xilinx-KC705-AXI-full-2016.1"

PROVIDES = "virtual/bitstream"

FILES_${PN} += "/boot/download.bit"

INHIBIT_DEFAULT_DEPS = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Copy the bitstream into the boot directory
do_install() {
	install -d ${D}/boot
	install ${S}/implementation/system.bit ${D}/boot/download.bit
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

