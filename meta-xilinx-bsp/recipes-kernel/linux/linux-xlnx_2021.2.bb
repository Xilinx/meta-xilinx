LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "6c52dbc03fbaf89bff90dde72accd06322bd54f4"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
