LINUX_VERSION = "5.15"
KBRANCH="xlnx_rebase_v5.15"
SRCREV = "1183ce490adb103e5e569b8ebd74c50c885ddc05"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
