LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "e6bf69e7e6010b6ced58f472c7d0d260f211b4c7"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
