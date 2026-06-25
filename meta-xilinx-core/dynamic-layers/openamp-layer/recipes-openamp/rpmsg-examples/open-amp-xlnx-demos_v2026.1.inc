require ${LAYER_PATH_openamp-layer}/recipes-openamp/rpmsg-examples/rpmsg-example.inc
require open-amp-xlnx-demos.inc

SRCREV = "9120de09988b142ac7807a1e018560510ed1082b"
BRANCH = "2026"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=ab88daf995c0bd0071c2e1e55f3d3505"
PV .= "+git"
REPO = "git://github.com/Xilinx/openamp-system-reference.git;protocol=https"

