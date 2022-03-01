LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "6a58fcf9a9592faeab019a3b55ce1f49ecfb91ea"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
