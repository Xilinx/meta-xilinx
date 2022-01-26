LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "a37f8cd486fd2ed22ccf9636d4616bb5f14b1b5d"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
