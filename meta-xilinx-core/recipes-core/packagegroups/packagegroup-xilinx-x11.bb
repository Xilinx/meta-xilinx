DESCRIPTION = "Xilinx X11 packages superset"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

X11_PACKAGES = " \
    packagegroup-core-x11 \
    packagegroup-core-x11-xserver \
    libxaw7 \
    libxpm \
    xclock \
    xcursor-transparent-theme \
    xeyes \
	"

RDEPENDS:${PN} = "${X11_PACKAGES}"
