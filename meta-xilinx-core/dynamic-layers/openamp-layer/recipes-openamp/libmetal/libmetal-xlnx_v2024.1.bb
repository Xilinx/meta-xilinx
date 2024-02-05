SRCBRANCH ?= "2024"
SRCREV = "b73ba4259aedde683c5d8105111dec8d0814ae79"
BRANCH = "2024"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=f4d5df0f12dcea1b1a0124219c0dbab4"
PV = "${SRCBRANCH}+git${SRCPV}"

REPO = "git://github.com/Xilinx/libmetal.git;protocol=https"

include ${LAYER_PATH_openamp-layer}/recipes-openamp/libmetal/libmetal.inc

RPROVIDES:${PN}-dbg += "libmetal-dbg"
RPROVIDES:${PN}-dev += "libmetal-dev"
RPROVIDES:${PN}-lic += "libmetal-lic"
RPROVIDES:${PN}-src += "libmetal-src"
RPROVIDES:${PN}-staticdev += "libmetal-staticdev"
