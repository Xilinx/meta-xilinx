LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "0496713417d18ef5f783238e7d2b029e74b3ba66"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
