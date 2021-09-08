LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "e4ac26253ae3a6f09884ae1b0bbb705a13ccd5e4"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
