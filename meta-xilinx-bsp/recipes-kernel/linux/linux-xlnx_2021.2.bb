LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "190ba47825b71dd7abeca27b890f1dd5bd5eb781"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
