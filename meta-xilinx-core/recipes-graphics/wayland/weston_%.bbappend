FILESEXTRAPATHS:prepend:zynqmp := "${THISDIR}/files:"

SRC_URI:append:zynqmp = " file://0001-libweston-Remove-substitute-format-for-ARGB8888.patch"

# Due to the SRC_URI zynqmp specific change, this needs to be SOC_FAMILY_ARCH specific
SOC_FAMILY_ARCH ??= "${TUNE_PKGARCH}"
PACKAGE_ARCH:zynqmp = "${SOC_FAMILY_ARCH}"


# OpenGL comes from libmali on ev/eg, when egl is enabled
DEPENDS:append:mali400 = "${@bb.utils.contains('PACKAGECONFIG', 'egl', ' libmali-xlnx', '', d)}"

# Store the default value
DEFAULT_PKGARCH:mali400 := "${PACKAGE_ARCH}"
# Set to machine_arch or default_pkgarch
PACKAGE_ARCH:mali400 = "${@bb.utils.contains('PACKAGECONFIG', 'egl', '${MACHINE_ARCH}', '${DEFAULT_PKGARCH}', d)}"

# Skip dmabuf-feedback, as it requires gbm >= 21.1.1, mali-xlnx only provides 17.3
SIMPLECLIENTS:mali400 ?= "damage,im,egl,shm,touch,dmabuf-v4l,dmabuf-egl"
