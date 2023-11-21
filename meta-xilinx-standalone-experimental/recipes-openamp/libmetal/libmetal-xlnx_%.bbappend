SRCREV = "00fd771adc7adaed664ed6c5bc3d48d25856fe5c"
REPO = "git://github.com/Xilinx/libmetal.git;protocol=https"
BRANCH = "xlnx_rel_v2023.2"
SRCBRANCH = "${BRANCH}"
PV = "${SRCBRANCH}+git${SRCPV}"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=f4d5df0f12dcea1b1a0124219c0dbab4"

IMPL = ""
IMPL:xilinx-standalone = "${LAYER_PATH_openamp-layer}/vendor/xilinx/meta-xilinx-standalone-experimental/recipes-openamp/libmetal/libmetal-xlnx-standalone.inc"
IMPL:xilinx-freertos = "${LAYER_PATH_openamp-layer}/vendor/xilinx/meta-xilinx-standalone-experimental/recipes-openamp/libmetal/libmetal-xlnx-standalone.inc"
require ${IMPL}
