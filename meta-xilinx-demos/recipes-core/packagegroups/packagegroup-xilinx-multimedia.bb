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
	packagegroup-xilinx-qt \
	ffmpeg \
	"

RDEPENDS:${PN} = "${MULTIMEDIA_PACKAGES}"
