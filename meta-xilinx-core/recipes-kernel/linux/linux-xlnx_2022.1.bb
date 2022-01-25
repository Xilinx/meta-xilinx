LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "8f3691e6b43c0f2c74584eebd1cd0b9645e525ae"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
