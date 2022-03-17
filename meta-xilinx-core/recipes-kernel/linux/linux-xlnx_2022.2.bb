LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "ae78bf6ba89f388361c223b89aed350b5ceee971"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
