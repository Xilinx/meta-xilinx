# This recipe tracks the 'bleeding edge' linux-xlnx repository.
# Since this tree is frequently updated, AUTOREV is used to track its contents.
#
# To enable this recipe, set PREFERRED_PROVIDER_virtual/kernel = "linux-yocto-dev"

KBRANCH = "master-next"
KBRANCH_DEFAULT = "${KBRANCH}"

include linux-xlnx.inc

# Use the SRCREV for the last tagged revision of linux-xlnx.
SRCREV = "f27f400f43062b28d2b6f0977e50492b851d7464"

python () {
    if d.getVar("PREFERRED_PROVIDER_virtual/kernel", True) != "linux-xlnx-dev":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-xlnx-dev to enable it")
    else:
        d.setVar("SRCREV", "${AUTOREV}")
}

LINUX_VERSION ?= "3.14+"
LINUX_VERSION_EXTENSION = "-xilinx-dev"
PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}+git${SRCPV}"
