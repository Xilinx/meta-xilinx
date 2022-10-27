FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://0001-DRI_Add_xlnx_dri.patch"

# This is not compatible with the mali400 driver, use mesa-gl instead
CONFLICT_MACHINE_FEATURES:class-target = "mali400"

PACKAGECONFIG:append:zynqmp-eg = " lima"
PACKAGECONFIG:append:zynqmp-ev = " lima"

PACKAGE_ARCH:zynqmp-eg = "${SOC_VARIANT_ARCH}"
PACKAGE_ARCH:zynqmp-ev = "${SOC_VARIANT_ARCH}"
