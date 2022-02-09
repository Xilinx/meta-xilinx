LINUX_VERSION = "5.15"
KBRANCH="xlnx_rebase_v5.15"
SRCREV = "423a108a01e05e84b59a4c4885c16bf3cd8c90c7"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
