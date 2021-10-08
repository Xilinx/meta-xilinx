LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "f95c57093dddc6c63c277fc1293ce6f688ecc72a"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
