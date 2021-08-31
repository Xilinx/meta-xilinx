LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "66627bb2d1c558fe2fbae0f371246c39dc3b213b"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
