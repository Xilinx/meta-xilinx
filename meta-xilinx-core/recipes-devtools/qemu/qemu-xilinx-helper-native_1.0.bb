
python () {
    if d.getVar("PREFERRED_PROVIDER_qemu-helper-native") != d.getVar("PN"):
        raise bb.parse.SkipRecipe("Set qemu-helper-native provider to use this recipe")
}

# TODO: improve this, since it is very hacky that this recipe need to build tunctl.
# include the existing qemu-helper-native
require recipes-devtools/qemu/qemu-helper-native_1.0.bb
# get the path to tunctl.c (from oe-core!)
FILESEXTRAPATHS:prepend := "${COREBASE}/meta/recipes-devtools/qemu/qemu-helper:"

# provide it, to replace the existing
PROVIDES += "qemu-helper-native"

# replace qemu with qemu-xilinx
DEPENDS:remove = "qemu-system-native"
DEPENDS:append = " \
		qemu-xilinx-system-native \
		qemu-xilinx-multiarch-helper-native \
		"

RDEPENDS:${PN}:remove = "qemu-system-native"
RDEPENDS:${PN}:append = " qemu-xilinx-system-native"
