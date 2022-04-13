LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "0b70857ca66da7d471f5c17d1af67a2af273a960"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
