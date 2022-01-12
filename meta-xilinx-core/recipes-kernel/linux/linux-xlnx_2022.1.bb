LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "6a698dbaaf0e6caa053476c2f661b36885a0ce30"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
