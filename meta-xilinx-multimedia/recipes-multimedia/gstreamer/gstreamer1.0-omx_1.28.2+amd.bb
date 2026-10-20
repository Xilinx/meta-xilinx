SUMMARY = "OpenMAX IL plugins for GStreamer"
DESCRIPTION = "Wraps available OpenMAX IL components and makes them available as standard GStreamer elements."
HOMEPAGE = "http://gstreamer.freedesktop.org/"
SECTION = "multimedia"

LICENSE = "LGPL-2.1-or-later"
LICENSE_FLAGS = "commercial"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c \
                    file://omx/gstomx.h;beginline=1;endline=21;md5=5c8e1fca32704488e76d2ba9ddfa935f"

# ---------------------------------------------------------------------------
# Source repository
# ---------------------------------------------------------------------------
REPO    ?= "git://github.com/Xilinx/gst-omx.git;protocol=https"
BRANCH  ?= "xlnx-rebase-v1.28.2"
SRCREV = "bd122a9c44985e4fa94bf64215b972e88b640ec1"
SRC_URI = "${REPO};branch=${BRANCH}"

PV .= "+git"

PE = "1"

# ---------------------------------------------------------------------------
# Build only for AMD SoCs carrying an Allegro OMX-IL core (VCU / VDU / VCU2)
# ---------------------------------------------------------------------------
inherit meson pkgconfig features_check
ANY_OF_MACHINE_FEATURES = "vcu vdu vcu2"

# ---------------------------------------------------------------------------
# Dependencies
# ---------------------------------------------------------------------------
DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad"
DEPENDS .= "${@bb.utils.contains('MACHINE_FEATURES', 'vcu',  ' libvcu-omxil',  '', d)}"
DEPENDS .= "${@bb.utils.contains('MACHINE_FEATURES', 'vdu',  ' libvdu-omxil',  '', d)}"
DEPENDS .= "${@bb.utils.contains('MACHINE_FEATURES', 'vcu2', ' libvcu2-omxil', '', d)}"

# ---------------------------------------------------------------------------
# OMX target and Allegro OMX-IL header dir, selected per SoC family
# ---------------------------------------------------------------------------
GSTREAMER_1_0_OMX_TARGET ?= "generic"
GSTREAMER_1_0_OMX_TARGET:zynqmp         = "zynqultrascaleplus"
GSTREAMER_1_0_OMX_TARGET:versal         = "versal"
GSTREAMER_1_0_OMX_TARGET:versal-2ve-2vm = "versalgen2"

OMX_HEADER_DIR               ?= ""
OMX_HEADER_DIR:zynqmp         = "vcu-omx-il"
OMX_HEADER_DIR:versal         = "vdu-omx-il"
OMX_HEADER_DIR:versal-2ve-2vm = "vcu2-omx-il"

EXTRA_OEMESON += "-Dtarget=${GSTREAMER_1_0_OMX_TARGET} -Dheader_path=${STAGING_INCDIR}/${OMX_HEADER_DIR}"

# Allegro cores share one library name and are machine-specific
GSTREAMER_1_0_OMX_CORE_NAME = "${libdir}/libOMX.allegro.core.so.1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

# ---------------------------------------------------------------------------
# Post-install: patch the installed gstomx.conf with the resolved core name
# ---------------------------------------------------------------------------
set_omx_core_name() {
	sed -i -e "s;^core-name=.*;core-name=${GSTREAMER_1_0_OMX_CORE_NAME};" \
		"${D}${sysconfdir}/xdg/gstomx.conf"
}
do_install[postfuncs] += " set_omx_core_name "

# ---------------------------------------------------------------------------
# Files
# ---------------------------------------------------------------------------
FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"
FILES:${PN}-staticdev += "${libdir}/gstreamer-1.0/*.a"

# ---------------------------------------------------------------------------
# Runtime dependencies
# ---------------------------------------------------------------------------
RDEPENDS:${PN}  = "${@bb.utils.contains('MACHINE_FEATURES', 'vcu',  'libvcu-omxil',  '', d)}"
RDEPENDS:${PN} .= "${@bb.utils.contains('MACHINE_FEATURES', 'vdu',  ' libvdu-omxil',  '', d)}"
RDEPENDS:${PN} .= "${@bb.utils.contains('MACHINE_FEATURES', 'vcu2', ' libvcu2-omxil', '', d)}"
