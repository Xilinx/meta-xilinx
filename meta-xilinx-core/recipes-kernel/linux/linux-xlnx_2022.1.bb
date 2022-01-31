LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "b39b62925e0cb5a0a54afd053ba3cc91893f2efd"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
