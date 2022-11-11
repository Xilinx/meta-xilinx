LINUX_VERSION = "5.15.0"
KBRANCH="master"
SRCREV = "8178dcd484c63a31f6dde4168be01af86bc705e4"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
