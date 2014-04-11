# This recipe allows for a 'bleeding edge' u-boot-xlnx build.
# Since this tree is frequently updated, AUTOREV is used to track its contents.
#
# To enable this recipe, set
# PREFERRED_VERSION_u-boot-xlnx ?= "${UBOOT_XLNX_DEV_BRANCH}"
# Alternatively to track and build master branch instead, set
# UBOOT_XLNX_DEV_BRANCH ?= "master"
# PREFERRED_VERSION_u-boot-xlnx ?= "${UBOOT_XLNX_DEV_BRANCH}"

UBOOT_XLNX_DEV_BRANCH ?= "master-next"

include u-boot-xlnx.inc

LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"

SRC_URI = "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https;branch=${UBOOT_XLNX_DEV_BRANCH}"

# Set default SRCREVs. SRCREVs statically set to prevent network access during 
# parsing.  
# AUTOREV is set in the anonymous python routine and resolved when the variables 
# are finalized.
SRCREV="f40924452f947fbd6886eaa677c1b0bd47edfcf5"

python () {
    d.setVar("SRCREV", "${AUTOREV}")
}

PR = "r0"
PV = "${UBOOT_XLNX_DEV_BRANCH}${XILINX_EXTENSION}+git"
