LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "e391aaed67e88a3c3c39cb6704efe5f9198eceec"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
