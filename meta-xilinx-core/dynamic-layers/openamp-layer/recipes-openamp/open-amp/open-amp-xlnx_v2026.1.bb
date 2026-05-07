SUMMARY = "AMD Xilinx OpenAMP (open-amp) library (xlnx fork)."
DESCRIPTION = "AMD Xilinx fork of the OpenAMP open-amp library, \
implementing VirtIO/RPMsg/Remoteproc on top of libmetal so Linux on \
the APU can boot and message firmware running on the RPU/PMU \
MicroBlaze cores of Zynq UltraScale+ MPSoC, Versal and Versal NET."
SRCBRANCH ?= "2026"
SRCREV = "2c90602ecb809c7b48d2d756318806c6e7666488"
BRANCH = "2026"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=dfc0adf4d04cc738ba65b7d3f587dca5"
PV .= "+git"
REPO = "git://github.com/Xilinx/open-amp.git;protocol=https"

include ${LAYER_PATH_openamp-layer}/recipes-openamp/open-amp/open-amp.inc
require ${LAYER_PATH_openamp-layer}/vendor/xilinx/recipes-openamp/open-amp/open-amp-xlnx.inc

RPROVIDES:${PN}-dbg += "open-amp-dbg"
RPROVIDES:${PN}-dev += "open-amp-dev"
RPROVIDES:${PN}-lic += "open-amp-lic"
RPROVIDES:${PN}-src += "open-amp-src"
RPROVIDES:${PN}-staticdev += "open-amp-staticdev"

