LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "4d2bbafd05b02d8d9399abc6a14c870ccc1f8c6e"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
