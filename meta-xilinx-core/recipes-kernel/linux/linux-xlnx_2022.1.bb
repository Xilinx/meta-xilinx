LINUX_VERSION = "5.15.19"
KBRANCH="xlnx_rebase_v5.15_LTS"
SRCREV = "031eb9ce665429a9574c95f89bcc488fd0ba0ec1"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
