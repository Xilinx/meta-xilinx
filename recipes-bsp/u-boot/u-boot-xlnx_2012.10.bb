
# We use the revision in order to avoid having to fetch it from the
# repo during parse
# Corresponds to github.com/Xilinx tag V14.4
SRCREV = "26786228acfdc0a02190a8d9ca9fcca51a5dcf28"
PV = "v2012.10${XILINX_EXTENSION}+git${SRCPV}"
PR = "r1"

include u-boot-xlnx.inc
