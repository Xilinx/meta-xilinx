LINUX_VERSION = "5.15.36"
KBRANCH="xlnx_rebase_v5.15_LTS"
SRCREV = "7e41fe9f1663e176fca4e76fef0c7e3057aee43b"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
