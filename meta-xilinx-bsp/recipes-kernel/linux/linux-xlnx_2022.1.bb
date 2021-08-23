LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "31be9028fac4ca0dd4c7cf182adfe666716fd64a"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
