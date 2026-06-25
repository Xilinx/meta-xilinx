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

# We need the flash type to specify other values, the machine is responsible
# for setting this.
QEMU_FLASH_TYPE ??= "undefined"

# Define default flash filename used for QEMU
QEMU_FLASH_FILE ?= "${@'qemu-${QEMU_FLASH_TYPE}' if d.getVar('QEMU_FLASH_TYPE') in [ 'qspi', 'ospi' ] else ''}"

# Build qemu SPI and empty image as necessary
EXTRA_IMAGEDEPENDS += "${@'qemu-image-empty:do_image_complete' if d.getVar('SOC_ON_DISK_BOOT_BIN') == '1' else ''}"
EXTRA_IMAGEDEPENDS += "${@'qemu-${QEMU_FLASH_TYPE}:do_deploy' if d.getVar('QEMU_FLASH_TYPE') in [ 'qspi', 'ospi' ] else ''}"

# Workaround to trigger runqemu to use -dtb to send the APU DTB
# This is done to ensure that we can easily change APU DTB at runtime via
# standard runqemu options.
# The file is not required by the wrapper, but if present is used
# to verify that the used boot.bin is the expected on.
QB_DEFAULT_KERNEL = "BOOT-${MACHINE}.bin"
QB_KERNEL_CMDLINE:microblaze = "none"
QB_KERNEL_CMDLINE:arm = "none"
QB_KERNEL_CMDLINE:aarch64 = "none"
