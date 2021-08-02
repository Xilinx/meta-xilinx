LINUX_VERSION = "5.10"
KBRANCH="xlnx_rebase_v5.10"
SRCREV = "cc84db65c53872c3beece8f401232232609b47de"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
