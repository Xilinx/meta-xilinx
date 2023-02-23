SECTION	= "libs"

REPO ?= "git://github.com/Xilinx/aie-rt.git;protocol=https"

BRANCH ?= "xlnx_rel_v2022.2"
SRCREV ?= "ae24c8ede049906323510693f37e6167a963fd0e"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM ?= "file://license.txt;md5=b81abf1f508952331dd7d2ab36408f5b"

SRC_URI = "${REPO};branch=${BRANCH}"
