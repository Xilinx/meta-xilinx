LINUX_VERSION = "5.15.19"
KBRANCH="xlnx_rebase_v5.15_LTS_2022.1_update"
SRCREV = "5ead03b6e64b5072737c653bb20ebcd88704e3d1"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
