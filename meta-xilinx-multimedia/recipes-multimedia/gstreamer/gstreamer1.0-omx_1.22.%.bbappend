require gstreamer-xilinx-1.22.%.inc

S = "${WORKDIR}/git/subprojects/gst-omx"

RDEPENDS:${PN} .= "${@bb.utils.contains('MACHINE_FEATURES', 'vcu', ' libvcu-omxil', '', d)}"
DEPENDS .= "${@bb.utils.contains('MACHINE_FEATURES', 'vcu', ' libvcu-omxil', '', d)}"

RDEPENDS:${PN} .= "${@bb.utils.contains('MACHINE_FEATURES', 'vdu', ' libvdu-omxil', '', d)}"
DEPENDS .= "${@bb.utils.contains('MACHINE_FEATURES', 'vdu', ' libvdu-omxil', '', d)}"

RDEPENDS:${PN} .= "${@bb.utils.contains('MACHINE_FEATURES', 'vcu2', ' libvcu2-omxil', '', d)}"
DEPENDS .= "${@bb.utils.contains('MACHINE_FEATURES', 'vcu2', ' libvcu2-omxil', '', d)}"

EXTRA_OECONF .= "${@bb.utils.contains('MACHINE_FEATURES', 'vcu', ' --with-omx-header-path=${STAGING_INCDIR}/vcu-omx-il', '', d)}"
EXTRA_OEMESON .= "${@bb.utils.contains('MACHINE_FEATURES', 'vcu', ' -Dheader_path=${STAGING_INCDIR}/vcu-omx-il', '', d)}"

EXTRA_OECONF .= "${@bb.utils.contains('MACHINE_FEATURES', 'vdu', ' --with-omx-header-path=${STAGING_INCDIR}/vdu-omx-il', '', d)}"
EXTRA_OEMESON .= "${@bb.utils.contains('MACHINE_FEATURES', 'vdu', ' -Dheader_path=${STAGING_INCDIR}/vdu-omx-il', '', d)}"

EXTRA_OECONF .= "${@bb.utils.contains('MACHINE_FEATURES', 'vcu2', ' --with-omx-header-path=${STAGING_INCDIR}/vcu2-omx-il', '', d)}"
EXTRA_OEMESON .= "${@bb.utils.contains('MACHINE_FEATURES', 'vcu2', ' -Dheader_path=${STAGING_INCDIR}/vcu2-omx-il', '', d)}"

DEFAULT_GSTREAMER_1_0_OMX_TARGET := "${GSTREAMER_1_0_OMX_TARGET}"
VCU_GSTREAMER_1_0_OMX_TARGET = "${@bb.utils.contains('MACHINE_FEATURES', 'vcu', 'zynqultrascaleplus', '${DEFAULT_GSTREAMER_1_0_OMX_TARGET}', d)}"
VDU_GSTREAMER_1_0_OMX_TARGET = "${@bb.utils.contains('MACHINE_FEATURES', 'vdu', 'versal', '${VCU_GSTREAMER_1_0_OMX_TARGET}', d)}"
VCU2_GSTREAMER_1_0_OMX_TARGET = "${@bb.utils.contains('MACHINE_FEATURES', 'vcu2', 'versalgen2', '${VDU_GSTREAMER_1_0_OMX_TARGET}', d)}"
GSTREAMER_1_0_OMX_TARGET = "${VCU2_GSTREAMER_1_0_OMX_TARGET}"

DEFAULT_GSTREAMER_1_0_OMX_CORE_NAME := "${GSTREAMER_1_0_OMX_CORE_NAME}"
GSTREAMER_1_0_OMX_CORE_NAME = "${@bb.utils.contains_any('MACHINE_FEATURES', 'vcu vdu vcu2', '${libdir}/libOMX.allegro.core.so.1', '${DEFAULT_GSTREAMER_1_0_OMX_CORE_NAME}', d)}"

DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
PACKAGE_ARCH[vardepsexclude] = "MACHINE_ARCH"
PACKAGE_ARCH = "${@bb.utils.contains_any('MACHINE_FEATURES', 'vcu vdu vcu2', '${MACHINE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"
