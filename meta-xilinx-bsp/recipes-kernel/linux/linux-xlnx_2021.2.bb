LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "bbb327d31bd1e1ad54e281da9ef01495d0a1cb2a"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
