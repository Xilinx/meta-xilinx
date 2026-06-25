SUMMARY = "AMD Xilinx fork of TF-A (trusted-firmware-a) for Zynq \
UltraScale+ MPSoC, Versal and Versal NET."
# Xilinx-specific trusted-firmware-a configuration
# Include this after the base trusted-firmware-a.inc

inherit shared-manifest

MANIFEST_COMPONENT_NAME = "trusted-firmware-a"
MANIFEST_SRCREV_VARS = "SRCREV_tfa SRCREV"
