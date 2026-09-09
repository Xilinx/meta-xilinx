DESCRIPTION = "GStreamer encoder and decoder elements for the video codec IP \
cores, built on top of the VCU and VCU2 control software."
HOMEPAGE = "https://github.com/Xilinx/gst-plugins-vcu"
SECTION = "multimedia"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=624e7970a65c3e70b309116d8fb6c3d7"

SRC_URI = "git://github.com/Xilinx/gst-plugins-vcu.git;protocol=https;branch=main"
SRCREV = "40a54d0fdf3778361b780f797874527a9c60fa57"

PV = "0.1.0+git"

inherit meson pkgconfig features_check

# The variants are separate meson projects under subprojects/, and installation
# is disabled when they are configured as a meson subproject.
S = "${UNPACKDIR}/${BP}/subprojects/${VCU_VARIANT}"

# Only one variant is buildable per machine, so this is machine specific.
PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "gstreamer1.0 gstreamer1.0-plugins-base"

# The element names carry the product, not the IP vendor, so a pipeline written
# for one variant runs unchanged on the other.
EXTRA_OEMESON += " \
    -Delement_prefix=vcu \
    -Dallegro_include_path=${STAGING_INCDIR}/${VCU_CTRLSW_INCDIR} \
    -Dallegro_library_path=${STAGING_LIBDIR} \
    -Dtests=disabled \
    -Dexamples=disabled \
"

FILES:${PN} += "${libdir}/gstreamer-1.0/libgstvcucodec.so"
