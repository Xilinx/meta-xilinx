SRCBRANCH ?= "2024"
SRCREV = "9824dd0819e353befb6c833c2036f2355e3e36e1"
BRANCH = "2024"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=f4d5df0f12dcea1b1a0124219c0dbab4"
PV = "${SRCBRANCH}+git${SRCPV}"

REPO = "git://github.com/Xilinx/libmetal.git;protocol=https"

include ${LAYER_PATH_openamp-layer}/recipes-openamp/libmetal/libmetal.inc
include ${LAYER_PATH_openamp-layer}/vendor/xilinx/recipes-openamp/libmetal/libmetal-xlnx.inc

RPROVIDES:${PN}-dbg += "libmetal-dbg"
RPROVIDES:${PN}-dev += "libmetal-dev"
RPROVIDES:${PN}-lic += "libmetal-lic"
RPROVIDES:${PN}-src += "libmetal-src"
RPROVIDES:${PN}-staticdev += "libmetal-staticdev"
