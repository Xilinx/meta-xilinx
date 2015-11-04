# This recipe allows for a 'bleeding edge' u-boot-xlnx build.
# Since this tree is frequently updated, AUTOREV is used to track its contents.
#
# To enable this recipe, set the following in your machine or local.conf
#   PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-xlnx-dev"

UBRANCH ?= "master"

include u-boot-xlnx.inc
include u-boot-extra.inc

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

SRCREV ?= '${@oe.utils.conditional( \
           "PREFERRED_PROVIDER_virtual/bootloader","u-boot-xlnx-dev",    \
           "${AUTOREV}", "c0dc1a884dc3509fa3e12235c099f1e4edc24431", d)}'

PV = "${UBRANCH}${XILINX_EXTENSION}+git${SRCPV}"

do_compile_append() {
    # link u-boot-dtb.img to u-boot.img.
    if [ ! -e ${B}/u-boot-dtb.img ]; then
        ln -sf u-boot.img ${B}/u-boot-dtb.img
    fi
}
