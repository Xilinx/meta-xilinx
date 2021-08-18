LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "240fee7df5d74cce89155c9a6381e6755cb8d3f7"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
