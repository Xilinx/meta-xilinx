SUMMARY = "Superset of X11 server, client libraries and utilities for \
AMD Xilinx desktop-style boards."
DESCRIPTION = "Xilinx X11 packages superset"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

X11_PACKAGES = " \
    packagegroup-core-x11 \
    packagegroup-core-x11-xserver \
    ${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'libxaw7', '', d)} \
    libxpm \
    ${@bb.utils.contains('BBFILE_COLLECTIONS', 'openembedded-layer', 'xclock', '', d)} \
    xcursor-transparent-theme \
    xeyes \
	"

RDEPENDS:${PN} = "${X11_PACKAGES}"
