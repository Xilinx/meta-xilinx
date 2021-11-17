LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "e69d41ef0ad9e142de287f46dd743c320cc2ef52"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
