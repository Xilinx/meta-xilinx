LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "a1aeea51946be6fd71afcad68c40e843eca63919"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
