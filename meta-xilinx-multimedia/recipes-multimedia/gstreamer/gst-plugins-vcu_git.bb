SUMMARY = "Allegro DVT GStreamer plugin for the AMD VCU and VCU2"
DESCRIPTION = "GStreamer encoder and decoder elements for the video codec IP \
cores, built on top of the VCU and VCU2 control software."
HOMEPAGE = "https://github.com/Xilinx/gst-plugins-vcu"
SECTION = "multimedia"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=24d1a3275c7d0fe0eb1c75e3e664594a"

SRC_URI = "git://github.com/Xilinx/gst-plugins-vcu.git;protocol=https;branch=main"
SRCREV = "0b8f38e5195910426cf300a2257d104d920d1e25"

PV = "0.1.0+git"

inherit meson pkgconfig features_check

ANY_OF_MACHINE_FEATURES = "vcu vcu2"

# The enabled variants follow the machine features, so this is machine specific.
PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "gstreamer1.0 gstreamer1.0-plugins-base"

PACKAGECONFIG ?= " \
    ${@bb.utils.contains('MACHINE_FEATURES', 'vcu', 'vcu', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'vcu2', 'vcu2', '', d)} \
"

PACKAGECONFIG[vcu] = "-Dvcu=enabled -Dvcu_include_path=${STAGING_INCDIR}/vcu-ctrl-sw/include -Dvcu_library_path=${STAGING_LIBDIR},-Dvcu=disabled,vcu-ctrlsw,libvcu-ctrlsw kernel-module-vcu"
PACKAGECONFIG[vcu2] = "-Dvcu2=enabled -Dvcu2_include_path=${STAGING_INCDIR}/vcu2-ctrl-sw/include -Dvcu2_library_path=${STAGING_LIBDIR},-Dvcu2=disabled,vcu2-ctrlsw,libvcu2-ctrlsw kernel-module-vcu2"

FILES:${PN} += "${libdir}/gstreamer-1.0/libgstvcucodec.so"
