LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "85077014c1d2f88d7ab1c10ec7643c73c4da66ac"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
