LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "e7f54e8fcc4f5ee50288e843cf509353cdf1a89e"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
