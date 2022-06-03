LINUX_VERSION = "5.15.36"
KBRANCH="xlnx_rebase_v5.15_LTS"
SRCREV = "28cd4306cc939a3c769b245ae248b80a22aa097d"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
