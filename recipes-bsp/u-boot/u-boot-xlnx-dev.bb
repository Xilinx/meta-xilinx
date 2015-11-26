# This recipe allows for a 'bleeding edge' u-boot-xlnx build.
# Since this tree is frequently updated, AUTOREV is used to track its contents.
#
# To enable this recipe, set the following in your machine or local.conf
#   PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-xlnx-dev"

UBRANCH ?= "master"

include u-boot-xlnx.inc
include u-boot-extra.inc
include u-boot-spl-zynq-init.inc

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

SRCREV_DEFAULT = "4942ae4d03ee4ef4b2dd750d441f730150ee9288"
SRCREV ?= "${@oe.utils.conditional("PREFERRED_PROVIDER_virtual/bootloader", "u-boot-xlnx-dev", "${AUTOREV}", "${SRCREV_DEFAULT}", d)}"

PV = "${UBRANCH}${XILINX_EXTENSION}+git${SRCPV}"

# Newer versions of u-boot have support for these
HAS_PS7INIT ?= " \
		zynq_microzed_config \
		zynq_zed_config \
		zynq_zc702_config \
		zynq_zc706_config \
		"

do_compile_append() {
    # link u-boot-dtb.img to u-boot.img.
    if [ ! -e ${B}/u-boot-dtb.img ]; then
        ln -sf u-boot.img ${B}/u-boot-dtb.img
    fi
}
