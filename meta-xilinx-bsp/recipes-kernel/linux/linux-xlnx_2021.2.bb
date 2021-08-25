LINUX_VERSION = "5.10"
KBRANCH="master"
SRCREV = "45cd0074cdf1ddd710b28848e6a860b442babfcc"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
