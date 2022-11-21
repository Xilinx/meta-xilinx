# This include is used to setup default QEMU and qemuboot config for meta-xilinx
# machines.

PREFERRED_PROVIDER_qemu = "qemu-xilinx"
PREFERRED_PROVIDER_qemu-native = "qemu-xilinx-native"
PREFERRED_PROVIDER_qemu-system-native = "qemu-xilinx-system-native"
PREFERRED_PROVIDER_nativesdk-qemu = "nativesdk-qemu-xilinx"

# enable the overrides for the context of the conf only
MACHINEOVERRIDES =. "qemuboot-xilinx:"

# depend on qemu-helper-native, which will depend on QEMU
EXTRA_IMAGEDEPENDS += "qemu-system-native qemu-helper-native:do_addto_recipe_sysroot"

# Use the xilinx specific version for these users
IMAGE_CLASSES += "qemuboot-xilinx"
