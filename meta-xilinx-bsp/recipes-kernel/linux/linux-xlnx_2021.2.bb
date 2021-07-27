LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "7a1899cdc9db309bfcece6eb08ae2c4282226763"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
