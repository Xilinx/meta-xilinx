LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "42359bc57bbfee6dfb31a9bad31374c3927f280d"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
