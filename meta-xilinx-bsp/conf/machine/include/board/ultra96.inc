# Ultra96 items that need to be configured from zynqmp-generic
PACKAGE_ARCH_pn-linux-xlnx_ultra96 = "${BOARDVARIANT_ARCH}"
KERNEL_DEVICETREE_ultra96 = "xilinx/zynqmp-zcu100-revC.dtb"

# Enable bluetooth and wifi module
MACHINE_ESSENTIAL_EXTRA_RDEPENDS_append_ultra96 = " linux-firmware-wl18xx linux-firmware-ti-bt-wl180x"

