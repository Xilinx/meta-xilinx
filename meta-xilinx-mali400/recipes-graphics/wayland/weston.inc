SRC_URI:append:zynqmp = " file://0001-libweston-Remove-substitute-format-for-ARGB8888.patch"

# Due to the SRC_URI zynqmp specific change, this needs to be MACHINE_ARCH specific
PACKAGE_ARCH:zynqmp = "${MACHINE_ARCH}"
