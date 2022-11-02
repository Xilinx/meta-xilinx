SRCBRANCH ?= "2022.2"
SRCREV = "e50f1a61d6bd355e54f3d0bd709c8c3b9e8014c6"
BRANCH = "2022"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=0e6d7bfe689fe5b0d0a89b2ccbe053fa"
PV = "${SRCBRANCH}+git${SRCPV}"

require ${LAYER_PATH_openamp-layer}/recipes-openamp/open-amp/open-amp.inc
