LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "3d5b2d9474191738409b38f0aa195ccb5babb4e6"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
