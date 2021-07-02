LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "87367bb8a688f6fefdaadb90128ed9cf6de5032d"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
