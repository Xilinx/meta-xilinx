SRCBRANCH ?= "2022.2"
SRCREV = "9cb5bd0f9b697d27da059dd868981aaf271e11ec"
BRANCH = "2022"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=1ff609e96fc79b87da48a837cbe5db33"
PV = "${SRCBRANCH}+git${SRCPV}"

REPO = "git://github.com/Xilinx/libmetal.git;protocol=https"

require ${LAYER_PATH_openamp-layer}/recipes-openamp/libmetal/libmetal.inc
