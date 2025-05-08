FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

ENABLE_XEN_DTSI ?= ""
ENABLE_XEN_QEMU_DTSI ?= ""

XEN_EXTRA_DT_INCLUDE_FILES = ""
XEN_EXTRA_QEMU_DT_INCLUDE_FILES = ""

XEN_EXTRA_DT_INCLUDE_FILES:zynqmp = "zynqmp-xen.dtsi"
XEN_EXTRA_QEMU_DT_INCLUDE_FILES:zynqmp = "zynqmp-xen.dtsi zynqmp-xen-qemu.dtsi"

XEN_EXTRA_DT_INCLUDE_FILES:versal = "versal-xen.dtsi"
XEN_EXTRA_QEMU_DT_INCLUDE_FILES:versal = "versal-xen.dtsi versal-xen-qemu.dtsi"

XEN_EXTRA_DT_INCLUDE_FILES:versal-net = "versal-net-xen.dtsi"
XEN_EXTRA_QEMU_DT_INCLUDE_FILES:versal-net = "versal-net-xen.dtsi versal-net-xen-qemu.dtsi"

EXTRA_DT_INCLUDE_FILES:append = "${@' ${XEN_EXTRA_DT_INCLUDE_FILES}' if d.getVar('ENABLE_XEN_DTSI') == '1' and d.getVar('TARGET_OS') == 'linux' else ''}"
EXTRA_DT_INCLUDE_FILES:append = "${@' ${XEN_EXTRA_QEMU_DT_INCLUDE_FILES}' if d.getVar('ENABLE_XEN_QEMU_DTSI') == '1' and d.getVar('TARGET_OS') == 'linux' else ''}"
