LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "d25c42dcc2a9fed0d00f4f20eded4aa733d51d5b"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
