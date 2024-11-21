# Use libmetal for systems with AIE
# For versal devices with the ai-engine
PACKAGE_ARCH_orig := "${PACKAGE_ARCH}"
PACKAGE_ARCH = "${@bb.utils.contains('MACHINE_FEATURES', 'aie', '${MACHINE_ARCH}', '${PACKAGE_ARCH_orig}', d)}"
EXTRA_OECMAKE .= "${@bb.utils.contains('MACHINE_FEATURES', 'aie', ' -DXRT_AIE_BUILD=true', '', d)}"
TARGET_CXXFLAGS .= "${@bb.utils.contains('MACHINE_FEATURES', 'aie', ' -DXRT_ENABLE_AIE -DFAL_LINUX=on', '', d)}"
DEPENDS .= "${@bb.utils.contains('MACHINE_FEATURES', 'aie', ' libxaiengine aiefal', '', d)}"
RDEPENDS:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'aie', ' libxaiengine aiefal', '', d)}"
