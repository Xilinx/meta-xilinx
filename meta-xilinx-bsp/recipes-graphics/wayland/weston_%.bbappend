FILESEXTRAPATHS_prepend_zynqmp := "${THISDIR}/files:"

SRC_URI_append_zynqmp = " file://0001-libweston-Remove-substitute-format-for-ARGB8888.patch"

# OpenGL comes from libmali on ev/eg, when egl is enabled
DEPENDS_append_mali400 = "${@bb.utils.contains('PACKAGECONFIG', 'egl', ' libmali-xlnx', '', d)}"

# Due to the SRC_URI zynqmp specific change, this needs to be SOC_FAMILY_ARCH specific
SOC_FAMILY_ARCH ??= "${TUNE_PKGARCH}"
ZYNQMP_PKGARCH = "${SOC_FAMILY_ARCH}"
# But if egl is enabled, we also need to be SOC_VARIANT_ARCH specific due to libmali
ZYNQMP_PKGARCH_mali400 = "${@bb.utils.contains('PACKAGECONFIG', 'egl', '${SOC_VARIANT_ARCH}', '${SOC_FAMILY_ARCH}', d)}"

PACKAGE_ARCH_zynqmp = "${ZYNQMP_PKGARCH}"
