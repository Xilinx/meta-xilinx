LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "67d5e656238eddd8486cdf9a0632985aeeee7b51"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
