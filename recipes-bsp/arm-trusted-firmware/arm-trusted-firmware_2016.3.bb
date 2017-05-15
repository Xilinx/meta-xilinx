include arm-trusted-firmware.inc

XILINX_RELEASE_VERSION = "v2016.3"
SRCREV ?= "a9e3716615a23c78e3cdea5b5b2f840f89817cb1"

PV = "1.2-xilinx-${XILINX_RELEASE_VERSION}+git${SRCPV}"

