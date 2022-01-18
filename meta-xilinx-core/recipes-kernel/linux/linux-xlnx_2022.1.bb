LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "dc405906cdb8a89cd81b6ca4f438fe213c187cb2"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
