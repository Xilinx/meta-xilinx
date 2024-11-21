SUMMARY = "Python library for calculating contours of 2D quadrilateral grids"
HOMEPAGE = "https://github.com/contourpy/contourpy"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a051d5dfc6ccbc7bbe3e626d65156784"

DEPENDS += " \
	meson-native \
	python3-pybind11-native \
	"

SRC_URI[sha256sum] = "4d8908b3bee1c889e547867ca4cdc54e5ab6be6d3e078556814a22457f49423c"

inherit pypi python_mesonpy

RDEPENDS:${PN} = "python3-numpy"
