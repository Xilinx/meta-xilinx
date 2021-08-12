LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "af88f405134da108f814cfdf5eac9f2b60f2b800"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
