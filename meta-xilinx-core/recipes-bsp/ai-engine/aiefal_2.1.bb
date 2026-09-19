SUMMARY = "AMD AI Engine Functional Abstraction Layer (AIEFAL)"
DESCRIPTION = "AMD AI Engine Function Abstraction Layer library, \
providing a high level API on top of the low-level AIE \
driver for AI Engine IP."

require aie-rt-2026.inc

SRC_URI += "file://0001-CMakeLists.txt-Add-cmake_minimum_required.patch"

SECTION	= "devel"

XAIEFAL_DIR ?= "fal"

IOBACKENDS ?= "Linux"

WITH_EXAMPLES ?= "0"

PROVIDES = "aiefal"
ALLOW_EMPTY:${PN} = "1"

inherit pkgconfig cmake ccmake

AIEFAL_DEPENDS = ""
AIEFAL_DEPENDS:linux = "libxaiengine"

DEPENDS = "${AIEFAL_DEPENDS}"

OECMAKE_SOURCEPATH = "${S}/${XAIEFAL_DIR}"

EXTRA_OECMAKE = "-DWITH_TESTS=OFF"
EXTRA_OECMAKE += "${@' -DWITH_EXAMPLES=ON' if d.getVar('WITH_EXAMPLES') in [ '1', 'y' ] else '-DWITH_EXAMPLES=OFF'}"

ENABLE_LINUX = ""
ENABLE_LINUX:linux = " -DFAL_LINUX=ON"
EXTRA_OECMAKE .= "${ENABLE_LINUX}"

OECMAKE_ARGS += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"

FILES:${PN}-demos = " \
    ${bindir}/* \
"
