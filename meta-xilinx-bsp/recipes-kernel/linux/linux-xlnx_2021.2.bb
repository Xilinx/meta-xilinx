LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "c977ad8fb7ec282f8a309e6eb1c12cd271ea2b68"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
