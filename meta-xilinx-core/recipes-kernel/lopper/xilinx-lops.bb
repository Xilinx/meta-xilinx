SUMMARY = "Device tree lopper - lops"
DESCRIPTION = "Xilinx specific lop files"
SECTION = "bootloader"
LICENSE = "BSD-3-Clause"

RDEPENDS:${PN} += "lopper"

SRC_URI = " \
	file://lop-microblaze-yocto.dts \
	file://lop-xilinx-id-cpus.dts \
	file://lop-machine-name.dts \
	"

LIC_FILES_CHKSUM = "file://lop-microblaze-yocto.dts;endline=10;md5=27139f9b862dc6fe466c7157aba7ed9c"

S = "${WORKDIR}"

inherit python3-dir

do_configure[noexec] = '1'
do_compile[noexec] = '1'

do_install() {
	mkdir -p ${D}/${PYTHON_SITEPACKAGES_DIR}/lopper/lops
	cp ${S}/lop-microblaze-yocto.dts ${D}/${PYTHON_SITEPACKAGES_DIR}/lopper/lops/.
	cp ${S}/lop-xilinx-id-cpus.dts   ${D}/${PYTHON_SITEPACKAGES_DIR}/lopper/lops/.
	cp ${S}/lop-machine-name.dts     ${D}/${PYTHON_SITEPACKAGES_DIR}/lopper/lops/.
}

FILES:${PN} += "${PYTHON_SITEPACKAGES_DIR}/lopper/lops"
BBCLASSEXTEND = "native nativesdk"
