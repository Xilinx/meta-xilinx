# This recipe tracks the 'bleeding edge' linux-xlnx repository.
# Since this tree is frequently updated, AUTOREV is used to track its contents.
#
# To enable this recipe, set PREFERRED_PROVIDER_virtual/kernel = "linux-yocto-dev"

# These variables should reflect the linux-xlnx SRCREV and Linux version of
# latest upstream linux release merge. If these are out of date, please email 
# meta-xilinx@yoctoproject.org to have it corrected. The linux-xlnx-dev build 
# should still function correctly with out of date values, but the naming will 
# be missleading.
LINUX_XLNX_NEARTOP_SRCREV = "669ee45083e22963d7fb7b774c5d7893ed35de2e"
LINUX_XLNX_NEARTOP_VERSION = "3.10"

# LINUX_VERSION is set to select the defconfig and dts we wish to use within
# meta-xilinx. If the build fails it may be because this defconfig is out 
# of date. If that happens or if the developer desires a non meta-xilinx config 
# they should add the defconfig and name it appropriately so the recipes find 
# the file.
LINUX_VERSION ?= "3.8"

LINUX_VERSION_EXTENSION ?= "-xilinx-dev"

KBRANCH ?= "master-next"
KBRANCH_DEFAULT = "master"

include linux-xlnx.inc

SRC_URI = "git://github.com/Xilinx/linux-xlnx.git;protocol=git;branch=${KBRANCH}"

# Set default SRCREVs. SRCREVs statically set to prevent network access during 
# parsing. If linux-xlnx-dev is the preferred provider, they will be overridden 
# to AUTOREV in the anonymous python routine and resolved when the variables 
# are finalized.
SRCREV="${LINUX_XLNX_NEARTOP_SRCREV}"

python () {
    if d.getVar("PREFERRED_PROVIDER_virtual/kernel", True) != "linux-xlnx-dev":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-xlnx-dev to enable it")
    else:
        d.setVar("SRCREV", "${AUTOREV}")
}

PR = "r0"
PV = "${LINUX_XLNX_NEARTOP_VERSION}+${LINUX_VERSION_EXTENSION}+git${SRCREV}"
