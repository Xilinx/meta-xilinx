SUMMARY = "AMD AI Engine Functional Abstraction Layer (AIEFAL)"
DESCRIPTION = "AMD AI Engine Function Abstraction Layer library, \
providing a high level API on top of the low-level AIE \
driver for AI Engine IP."

require aie-rt-2026.inc

SECTION	= "devel"

XAIEFAL_DIR ?= "fal"

IOBACKENDS ?= "Linux"

WITH_EXAMPLES ?= "0"

PROVIDES = "aiefal"
ALLOW_EMPTY:${PN} = "1"

inherit pkgconfig cmake

DEPENDS = "libxaiengine"

OECMAKE_SOURCEPATH = "${S}/${XAIEFAL_DIR}"

EXTRA_OECMAKE = "-DWITH_TESTS=OFF  -DFAL_LINUX=ON "
EXTRA_OECMAKE += "${@'-DWITH_EXAMPLES=ON' if d.getVar('WITH_EXAMPLES') in [ '1', 'y' ] else '-DWITH_EXAMPLES=OFF'}"
OECMAKE_ARGS += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"

FILES:${PN}-demos = " \
    ${bindir}/* \
"
