SUMMARY = "PMU ROM for QEMU"
DESCRIPTION = "The ZynqMP PMU ROM for QEMU emulation"
HOMEPAGE = "http://www.xilinx.com"
SECTION = "bsp"

LICENSE = "Proprietary"
LICENSE_FLAGS = "xilinx"
LIC_FILES_CHKSUM = "file://PMU_ROM-LICENSE.txt;md5=d43d49bc1eb1c907fc6f4ea75abafdfc"

SRC_URI = "https://www.xilinx.com/bin/public/openDownload?filename=PMU_ROM.tar.gz"
SRC_URI[sha256sum] = "f9a450ef960979463ea0a87a35fafb4a5b62d3a741de30cbcef04c8edc22a7cf"

S = "${WORKDIR}/PMU_ROM"

inherit deploy native

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"

do_deploy () {
	install -D ${S}/pmu-rom.elf ${DEPLOYDIR}/pmu-rom.elf
}

addtask deploy before do_build after do_install
