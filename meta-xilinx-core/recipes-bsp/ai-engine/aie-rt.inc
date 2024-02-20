SECTION	= "libs"

REPO ?= "git://github.com/Xilinx/aie-rt.git;protocol=https"

BRANCH ?= "main-aie"
SRCREV ?= "a6e2fe31b0307fa952ac47108c4b8c869480bae7"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM ?= "file://license.txt;md5=04a153cae61a8a606fc79dff49c2c897"

SRC_URI = "${REPO};branch=${BRANCH}"
