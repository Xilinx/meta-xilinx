SUMMARY = "Packagegroup pulling in the AMD Xilinx out-of-box \
multimedia stack (GStreamer, hardware-accelerated codecs, helper \
apps)."
DESCRIPTION = "Packages for out of box multimedia experience"

# Can not be all arch due to libdrm
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup features_check

REQUIRED_DISTRO_FEATURES = "x11"

MULTIMEDIA_PACKAGES = " \
	packagegroup-xilinx-gstreamer \
	packagegroup-xilinx-matchbox \
	packagegroup-core-x11 \
	libdrm \
	libdrm-tests \
	ffmpeg \
	"

RDEPENDS:${PN} = "${MULTIMEDIA_PACKAGES}"
