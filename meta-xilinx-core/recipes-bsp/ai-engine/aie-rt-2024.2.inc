SECTION	= "libs"

REPO ?= "git://github.com/Xilinx/aie-rt.git;protocol=https"

BRANCH ?= "xlnx_rel_v2024.2"
SRCREV ?= "f0d1b5bff185bd1d95a5c1b294ba8e8418dcd151"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM ?= "file://license.txt;md5=04a153cae61a8a606fc79dff49c2c897"

SRC_URI = "${REPO};branch=${BRANCH}"
