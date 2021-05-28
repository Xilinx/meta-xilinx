LINUX_VERSION = "5.10"
KBRANCH="xlnx_rebase_v5.10"
SRCREV = "f5e7411defe2b6e69fe0d933150f63f6292d588c"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
