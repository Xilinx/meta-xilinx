LINUX_VERSION = "5.15.36"
KBRANCH="xlnx_rebase_v5.15_LTS"
SRCREV = "0666ebfb0c32e2af2e2e943ca1561a9a0f83ee9f"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
