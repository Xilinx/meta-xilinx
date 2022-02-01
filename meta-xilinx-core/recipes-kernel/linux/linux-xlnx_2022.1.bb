LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "c399f40e7bda1f74473cfc43f7c90dc7ee15d998"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
