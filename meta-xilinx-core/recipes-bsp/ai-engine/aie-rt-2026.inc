SECTION	= "libs"

REPO ?= "git://github.com/Xilinx/aie-rt.git;protocol=https"

BRANCH ?= "xlnx_rel_v2026.1"
SRCREV ?= "6ee6a4da5f55bd66d278d5032108f0ebe920a501"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM ?= "file://license.txt;md5=04a153cae61a8a606fc79dff49c2c897"

SRC_URI = "${REPO};branch=${BRANCH}"
