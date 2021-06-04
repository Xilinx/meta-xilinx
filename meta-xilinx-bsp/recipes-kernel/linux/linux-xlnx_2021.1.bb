LINUX_VERSION = "5.10"
KBRANCH="xlnx_rebase_v5.10"
SRCREV = "cd1a972ed275ace731d851a8c88f575456262e75"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
