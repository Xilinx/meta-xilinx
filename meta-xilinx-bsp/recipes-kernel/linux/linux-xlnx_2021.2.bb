LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "6e1cc0eca7a45e5a24e70b7b203c3b7e5647b0f0"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
