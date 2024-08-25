FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# device tree sources for the various machines
COMPATIBLE_MACHINE:qemu-zynq7 = ".*"
SRC_URI:append:qemu-zynq7 = " file://qemu-zynq7.dts"

EXTRA_OVERLAYS:append:vek280-versal = " system-vek280.dtsi"

SDFEC_EXTRA_OVERLAYS ??= ""
SDFEC_EXTRA_OVERLAYS:zcu111-zynqmp = "system-zcu111.dtsi"

EXTRA_OVERLAYS:append:zcu111-zynqmp = "${@' ${SDFEC_EXTRA_OVERLAYS}' if d.getVar('ENABLE_SDFEC_DT') == '1' else ''}"
