LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "67f85522c7cb52e4a3bf31275ffcf0e2c90c769a"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
