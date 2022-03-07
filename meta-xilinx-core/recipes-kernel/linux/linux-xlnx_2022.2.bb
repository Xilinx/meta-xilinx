LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "ef4ac9cad9406e869e7c20191f3a3b0bb9663d81"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
