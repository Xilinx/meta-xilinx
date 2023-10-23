FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# device tree sources for the various machines
COMPATIBLE_MACHINE:qemu-zynq7 = ".*"
SRC_URI:append:qemu-zynq7 = " file://qemu-zynq7.dts"

EXTRA_OVERLAYS:append = "${@bb.utils.contains('MACHINE_FEATURES', 'provencore', ' pnc.dtsi', '', d)}"
