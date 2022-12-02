LINUX_VERSION = "5.15.0"
KBRANCH="master"
SRCREV = "015bf4737b50f4b3f7a5a8ed60c41bcb245618f5"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
