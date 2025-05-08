# This include is used to setup default QEMU and qemuboot config for meta-xilinx
# machines.

PREFERRED_PROVIDER_qemu-native = "qemu-xilinx-native"
PREFERRED_PROVIDER_qemu-system-native = "qemu-xilinx-system-native"
PREFERRED_PROVIDER_nativesdk-qemu = "nativesdk-qemu-xilinx"

# enable the overrides for the context of the conf only
MACHINEOVERRIDES =. "qemuboot-xilinx:"

# depend on qemu-helper-native, which will depend on QEMU
EXTRA_IMAGEDEPENDS += "qemu-devicetrees-native qemu-system-native qemu-helper-native:do_addto_recipe_sysroot"

# Use the xilinx specific version for these users
IMAGE_CLASSES += "qemuboot-xilinx"

# As of Yocto Project nanbield, if a -serial is pass in QB_OPT_APPEND the
# runqemu may add additional null entries or simply skip further setup.
#
# To help us be able to adjust for this behavior add a special
# QEMU_HW_SERIAL that will allow us to define serial ports for qemu
# emulated boards that may not match the standard Linux behavior.
#
QEMU_HW_SERIAL ??= ""
QB_OPT_APPEND += "${QEMU_HW_SERIAL} -nodefaults"
QB_SERIAL_OPT = ""

# Workaround to trigger runqemu to use -dtb to send the APU DTB
# This is done to ensure that we can easily change APU DTB at runtime via
# standard runqemu options.  (only used on multiarch systems)
# The file is not actually used, but it does have to exist.
QB_DEFAULT_KERNEL:aarch64 = "BOOT-${MACHINE}.bin"
QB_KERNEL_CMDLINE:aarch64 = "none"
