LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "f35044ea63711d00090f38116a67ffe51ddcea8c"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
