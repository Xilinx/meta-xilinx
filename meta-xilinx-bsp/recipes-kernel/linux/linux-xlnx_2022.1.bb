LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "d34cb5d4eed373ced1f10c3fe9d23237c5d902bc"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
