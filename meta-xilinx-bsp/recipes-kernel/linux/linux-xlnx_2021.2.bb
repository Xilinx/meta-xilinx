LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "8b1d6f1f08cd67a6ce548f522fc67cf182c77784"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
