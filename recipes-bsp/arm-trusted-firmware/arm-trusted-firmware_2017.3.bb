include arm-trusted-firmware.inc

XILINX_RELEASE_VERSION = "v2017.3"
SRCREV ?= "f9b244beaa7ac6a670b192192b6e92e5fd6044dc"

PV = "1.3-xilinx-${XILINX_RELEASE_VERSION}+git${SRCPV}"
