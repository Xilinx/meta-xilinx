LINUX_VERSION = "5.10"
KBRANCH="xlnx_rebase_v5.10"
SRCREV = "6f887a3e4f2e3a52b4591879f46a233a707b1ba3"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
