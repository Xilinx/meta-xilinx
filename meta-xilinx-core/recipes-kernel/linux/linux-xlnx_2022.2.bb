LINUX_VERSION = "5.15.36"
KBRANCH="xlnx_rebase_v5.15_LTS"
SRCREV = "32a31b33e66956fb2c0956c683530498fd1ebacd"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
