LINUX_VERSION = "5.10"
KBRANCH="xlnx_rebase_v5.10"
SRCREV = "46b615949d9f1ddecbcf110af6bcb3e83035c57d"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
