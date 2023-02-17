SRCBRANCH ?= "2023.1"
SRCREV = "c8aaf2f26d5493f492f0af09dd558d45908636da"
BRANCH = "2023"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=ab88daf995c0bd0071c2e1e55f3d3505"
PV = "${SRCBRANCH}+git${SRCPV}"
REPO = "git://github.com/Xilinx/open-amp.git;protocol=https"

include ${LAYER_PATH_openamp-layer}/recipes-openamp/open-amp/open-amp.inc
require ${LAYER_PATH_openamp-layer}/vendor/xilinx/recipes-openamp/open-amp/open-amp-xlnx.inc
