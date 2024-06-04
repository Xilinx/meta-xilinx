SUMMARY = "Xilinx AI Engine FAL(Functional Abstraction Layer)"
DESCRIPTION = "AIE FAL provides functional abstraction APIs for runtime support of Xilinx AI Engine IP"

require aie-rt-2022.inc

SECTION	= "devel"

XAIEFAL_DIR ?= "fal"
S = "${WORKDIR}/git"

inherit features_check

REQUIRED_MACHINE_FEATURES = "aie"

PACKAGE_ARCH = "${MACHINE_ARCH}"

IOBACKENDS ?= "Linux"

PROVIDES = "aiefal"
ALLOW_EMPTY:${PN} = "1"

inherit pkgconfig cmake

DEPENDS = "libxaiengine"

OECMAKE_SOURCEPATH = "${S}/${XAIEFAL_DIR}"

EXTRA_OECMAKE = "-DWITH_TESTS=OFF "
EXTRA_OECMAKE:append = "${@'-DWITH_EXAMPLES=ON' if d.getVar('WITH_EXAMPLES') == 'y' else '-DWITH_EXAMPLES=OFF'}"

FILES:${PN}-demos = " \
    ${bindir}/* \
"
