LINUX_VERSION = "5.15.19"
KBRANCH="xlnx_rebase_v5.15_LTS_2022.1_update"
SRCREV = "da106dbe4de690c78651d095cd696f204c857f84"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
