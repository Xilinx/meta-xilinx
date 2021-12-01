# Ultra96 items that need to be configured from zynqmp-generic
PACKAGE_ARCH:pn-linux-xlnx:ultra96 = "${BOARDVARIANT_ARCH}"
KERNEL_DEVICETREE:ultra96 = "xilinx/zynqmp-zcu100-revC.dtb"

# Enable bluetooth and wifi module
#MACHINE_ESSENTIAL_EXTRA_RDEPENDS:append:ultra96 = " linux-firmware-wl18xx linux-firmware-ti-bt-wl181x"

