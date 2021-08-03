LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "901332ef2f6c6f5bab8a931cb0e5dbde0252d953"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
