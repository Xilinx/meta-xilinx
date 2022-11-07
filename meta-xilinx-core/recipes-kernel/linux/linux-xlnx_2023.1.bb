LINUX_VERSION = "5.15.0"
KBRANCH="master"
SRCREV = "b754af5dade02518c3b0577f3066c3717bb17aa6"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
