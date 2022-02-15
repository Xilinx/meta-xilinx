LINUX_VERSION = "5.15"
KBRANCH="xlnx_rebase_v5.15"
SRCREV = "66bb31d008b55b6ae2ca9e41f7a4ff21816dded3"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
