LINUX_VERSION = "5.15.36"
KBRANCH="xlnx_rebase_v5.15_LTS"
SRCREV = "86e63ba3941dcd378d7626b21ad31e779b40fa73"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
