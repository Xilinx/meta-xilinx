DESCRIPTION = "PetaLinux opencv supported packages"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

OPENCV_PACKAGES = " \
	opencv \
	libopencv-core \
	libopencv-highgui \
	libopencv-imgproc \
	libopencv-objdetect \
	libopencv-ml \
	libopencv-calib3d \
	libopencv-ccalib \
	"

RDEPENDS:${PN} = "${OPENCV_PACKAGES}"
