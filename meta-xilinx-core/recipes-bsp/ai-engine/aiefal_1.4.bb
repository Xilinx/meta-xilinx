SUMMARY = "Xilinx AI Engine FAL(Functional Abstraction Layer)"
DESCRIPTION = "AIE FAL provides functional abstraction APIs for runtime support of Xilinx AI Engine IP"

require aie-rt-2022.inc

SECTION	= "devel"

XAIEFAL_DIR ?= "fal"
S = "${UNPACKDIR}/git"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal-ai-core = "versal-ai-core"
COMPATIBLE_MACHINE:versal-ai-edge = "${SOC_VARIANT_ARCH}"

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

PACKAGE_ARCH:versal-ai-core = "${SOC_VARIANT_ARCH}"
