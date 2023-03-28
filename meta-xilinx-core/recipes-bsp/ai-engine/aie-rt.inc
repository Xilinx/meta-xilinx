SECTION	= "libs"

REPO ?= "git://github.com/Xilinx/aie-rt.git;protocol=https"

BRANCH ?= "xlnx_rel_v2023.1"
SRCREV ?= "24d56bb3df32c3e4b6ac1a7930b0c788a3453d58"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM ?= "file://license.txt;md5=b81abf1f508952331dd7d2ab36408f5b"

SRC_URI = "${REPO};branch=${BRANCH}"
