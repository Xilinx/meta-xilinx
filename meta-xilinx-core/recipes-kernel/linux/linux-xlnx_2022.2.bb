LINUX_VERSION = "5.15.19"
KBRANCH="xlnx_rebase_v5.15_LTS"
SRCREV = "3029941a1c223de78d0d728286a1ea915f2ffaa3"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
