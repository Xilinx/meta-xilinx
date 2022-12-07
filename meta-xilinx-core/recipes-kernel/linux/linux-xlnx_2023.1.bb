LINUX_VERSION = "5.15.0"
KBRANCH="master"
SRCREV = "a876c5230ea6ad568ddf2443bdf0e05c655a0236"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
