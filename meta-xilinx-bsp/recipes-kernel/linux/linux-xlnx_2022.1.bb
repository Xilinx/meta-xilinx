LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "3fbb3a6c57a43342a7daec3bbae2f595c50bc969"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
