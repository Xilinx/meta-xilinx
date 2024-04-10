SRCBRANCH ?= "2024"
SRCREV = "7d39410ad2172be9f339c4ce565ed765ddd8c5c8"
BRANCH = "xlnx_rel_v2024.1"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=ab88daf995c0bd0071c2e1e55f3d3505"
PV = "${SRCBRANCH}+git${SRCPV}"
REPO = "git://github.com/Xilinx/open-amp.git;protocol=https"

include ${LAYER_PATH_openamp-layer}/recipes-openamp/open-amp/open-amp.inc
require ${LAYER_PATH_openamp-layer}/vendor/xilinx/recipes-openamp/open-amp/open-amp-xlnx.inc

RPROVIDES:${PN}-dbg += "open-amp-dbg"
RPROVIDES:${PN}-dev += "open-amp-dev"
RPROVIDES:${PN}-lic += "open-amp-lic"
RPROVIDES:${PN}-src += "open-amp-src"
RPROVIDES:${PN}-staticdev += "open-amp-staticdev"

