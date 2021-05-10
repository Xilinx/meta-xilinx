LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "bea04171cb9e59f85602890504a91f4d1321df21"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
