FILESEXTRAPATHS:prepend := "${COREBASE}/meta/recipes-devtools/qemu/qemu-helper:"

require ${COREBASE}/meta/recipes-devtools/qemu/qemu-helper-native_1.0.bb

# provide it, to replace the existing recipe
PROVIDES = "qemu-helper-native"

# replace qemu with qemu-xilinx
DEPENDS:remove = "qemu-system-native"
DEPENDS:append = " qemu-xilinx-system-native qemu-xilinx-multiarch-helper-native"

RDEPENDS:${PN}:remove = "qemu-system-native"
RDEPENDS:${PN}:append = " qemu-xilinx-system-native"
