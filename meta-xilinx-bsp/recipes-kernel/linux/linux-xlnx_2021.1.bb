LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "2c67a02cdd8efb00e679c5ae3ffe25d3fa710840"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
