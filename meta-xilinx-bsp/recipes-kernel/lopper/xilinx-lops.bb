SUMMARY = "Device tree lopper - lops"
DESCRIPTION = "Xilinx specific lop files"
SECTION = "bootloader"
LICENSE = "BSD-3-Clause"

RDEPENDS_${PN} += "lopper"

SRC_URI = "file://lop-microblaze-yocto.dts"

LIC_FILES_CHKSUM = "file://lop-microblaze-yocto.dts;endline=8;md5=a0e89d39fa397ec5d5320409ff701280"

S = "${WORKDIR}"

do_configure[noexec] = '1'
do_compile[noexec] = '1'

do_install() {
	mkdir -p ${D}/${datadir}/lopper/lops
	cp ${S}/lop-microblaze-yocto.dts ${D}/${datadir}/lopper/lops/.
}

FILES_${PN} += "${datadir}/lopper/lops/lop-microblaze-yocto.dts"
BBCLASSEXTEND = "native nativesdk"
