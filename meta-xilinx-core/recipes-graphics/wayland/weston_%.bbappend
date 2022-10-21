FILESEXTRAPATHS:prepend:zynqmp := "${THISDIR}/files:"

SRC_URI:append:zynqmp = " file://0001-libweston-Remove-substitute-format-for-ARGB8888.patch"

# OpenGL comes from libmali on ev/eg, when egl is enabled
DEPENDS:append:mali400 = "${@bb.utils.contains('PACKAGECONFIG', 'egl', ' libmali-xlnx', '', d)}"

# Due to the SRC_URI zynqmp specific change, this needs to be SOC_FAMILY_ARCH specific
SOC_FAMILY_ARCH ??= "${TUNE_PKGARCH}"
ZYNQMP_PKGARCH = "${SOC_FAMILY_ARCH}"
# But if egl is enabled, we also need to be SOC_VARIANT_ARCH specific due to libmali
ZYNQMP_PKGARCH:mali400 = "${@bb.utils.contains('PACKAGECONFIG', 'egl', '${SOC_VARIANT_ARCH}', '${SOC_FAMILY_ARCH}', d)}"

# Skip dmabuf-feedback, as it requires gbm >= 21.1.1, mali-xlnx only provides 17.3
SIMPLECLIENTS:mali400 ?= "damage im egl shm touch dmabuf-v4l dmabuf-egl"

PACKAGE_ARCH:zynqmp = "${ZYNQMP_PKGARCH}"
