LINUX_VERSION = "5.15.19"
KBRANCH="xlnx_rebase_v5.15_LTS"
SRCREV = "0553d8b86ffeb2035c2cef02085d9a9969bfb409"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
