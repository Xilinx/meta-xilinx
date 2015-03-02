# This recipe allows for a 'bleeding edge' u-boot-xlnx build.
# Since this tree is frequently updated, AUTOREV is used to track its contents.
#
# To enable this recipe, set the following in your machine or local.conf
#   PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-xlnx-dev"
#   PREFERRED_PROVIDER_u-boot ?= "u-boot-xlnx-dev"

UBRANCH ?= "master-next"

include u-boot-xlnx.inc
include u-boot-elf.inc
include u-boot-extra.inc

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

SRCREV="67f6167a0f808ca7051a337858e0db237ce2b972"

python () {
    d.setVar("SRCREV", "${AUTOREV}")
}

PR = "r0"
PV = "${UBRANCH}${XILINX_EXTENSION}+git"
