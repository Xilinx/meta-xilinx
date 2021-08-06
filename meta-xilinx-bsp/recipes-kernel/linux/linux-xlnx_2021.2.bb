LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "feb9c4a9d3b278e059057a6d0dd3c74fa676f7b2"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
