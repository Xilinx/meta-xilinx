# This recipe allows for a 'bleeding edge' u-boot-xlnx build.
# Since this tree is frequently updated, AUTOREV is used to track its contents.
#
# To enable this recipe, set the following in your machine or local.conf
#   PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-xlnx-dev"

UBRANCH ?= "master"

include u-boot-xlnx.inc

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

SRCREV_DEFAULT = "4942ae4d03ee4ef4b2dd750d441f730150ee9288"
SRCREV ?= "${@oe.utils.conditional("PREFERRED_PROVIDER_virtual/bootloader", "u-boot-xlnx-dev", "${AUTOREV}", "${SRCREV_DEFAULT}", d)}"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"
SRC_URI_append_kc705-trd-microblazeel = " file://microblaze-kc705-trd-Convert-microblaze-generic-to-k.patch"

PV = "${UBRANCH}${XILINX_EXTENSION}+git${SRCPV}"

