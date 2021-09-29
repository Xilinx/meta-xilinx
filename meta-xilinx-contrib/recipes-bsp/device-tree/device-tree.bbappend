FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# device tree sources for MiniZed
COMPATIBLE_MACHINE_minized-zynq7 = ".*"
SRC_URI:append_minized-zynq7 = " file://minized-zynq7.dts"

