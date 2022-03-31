LINUX_VERSION = "5.15"
KBRANCH="master"
SRCREV = "345aa64fe04d1de0995cb41afc671b7ef499dffd"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
