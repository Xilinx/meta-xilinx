require ${LAYER_PATH_openamp-layer}/recipes-openamp/rpmsg-examples/rpmsg-example.inc
require open-amp-xlnx-demos.inc

SRCREV = "7dbe56cc759971a6b0c6e43f812ba96228bde589"
BRANCH = "2026"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=ab88daf995c0bd0071c2e1e55f3d3505"
PV .= "+git"
REPO = "git://github.com/Xilinx/openamp-system-reference.git;protocol=https"

