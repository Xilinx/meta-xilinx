SUMMARY = "Xilinx AI Engine FAL(Functional Abstraction Layer)"
DESCRIPTION = "AIE FAL provides functional abstraction APIs for runtime support of Xilinx AI Engine IP"

require aie-rt-2024.2.inc

SECTION	= "devel"

XAIEFAL_DIR ?= "fal"
S = "${WORKDIR}/git"

IOBACKENDS ?= "Linux"

PROVIDES = "aiefal"
ALLOW_EMPTY:${PN} = "1"

inherit pkgconfig cmake

DEPENDS = "libxaiengine"

OECMAKE_SOURCEPATH = "${S}/${XAIEFAL_DIR}"

EXTRA_OECMAKE = "-DWITH_TESTS=OFF  -DFAL_LINUX=ON "
EXTRA_OECMAKE:append = "${@'-DWITH_EXAMPLES=ON' if d.getVar('WITH_EXAMPLES') == 'y' else '-DWITH_EXAMPLES=OFF'}"

FILES:${PN}-demos = " \
    ${bindir}/* \
"
