LINUX_VERSION = "5.15.19"
KBRANCH="xlnx_rebase_v5.15_LTS"
SRCREV = "b0c1be301e78c320df8c4d93b18393bfd7fd4e9d"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
