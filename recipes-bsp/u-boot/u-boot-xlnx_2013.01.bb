
# We use the revision in order to avoid having to fetch it from the
# repo during parse
# Corresponds to github.com/Xilinx tag V14.5
SRCREV = "20a6cdd301941b97961c9c5425b5fbb771321aac"
PV = "v2013.01${XILINX_EXTENSION}+git${SRCPV}"
PR = "r1"

include u-boot-xlnx.inc
