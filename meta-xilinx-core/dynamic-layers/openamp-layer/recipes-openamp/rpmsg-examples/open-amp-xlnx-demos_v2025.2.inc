require ${LAYER_PATH_openamp-layer}/recipes-openamp/rpmsg-examples/rpmsg-example.inc
require open-amp-xlnx-demos.inc

SRCREV = "c4ede49150423a2cdb0e81503809cf1b6b4cef0a"
BRANCH = "xlnx_rel_v2025.2"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=ab88daf995c0bd0071c2e1e55f3d3505"
PV .= "+git"
REPO = "git://github.com/Xilinx/openamp-system-reference.git;protocol=https"

