LINUX_VERSION = "5.15.19"
KBRANCH="xlnx_rebase_v5.15_LTS"
SRCREV = "9a70eb57165702913ca8eb37ffddd2b8bd03713c"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
