LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "9c323504dcd43ccb488ca2071672591b1a6cfbd4"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
