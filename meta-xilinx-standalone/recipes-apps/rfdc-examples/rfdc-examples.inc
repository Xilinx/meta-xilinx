LICENSE = "BSD"

inherit xlnx-embeddedsw features_check

REQUIRED_MACHINE_FEATURES = "rfsoc"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "librfdc"
RDEPENDS:${PN} = "librfdc"

PACKAGES = "${PN}-dbg ${PN}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

FLAG = ""
FLAG:zcu111-zynqmp = "-DXPS_BOARD_ZCU111"

RFDC_SUBDIR = "XilinxProcessorIPLib/drivers/rfdc/examples"

do_compile:prepend () {
    cd ${S}/${RFDC_SUBDIR}
    cp Makefile.Linux Makefile
}
