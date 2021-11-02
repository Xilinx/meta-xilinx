LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "0a88ef03d3015782318b4bc94ceb20dca375a01b"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
