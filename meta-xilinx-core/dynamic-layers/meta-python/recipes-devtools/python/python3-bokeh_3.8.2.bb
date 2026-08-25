SUMMARY = "Interactive plots and applications in the browser from \
Python"
DESCRIPTION = "Bokeh interactive visualization library: build \
browser-deliverable HTML/JS plots, dashboards and apps from Python, \
used here by AMD's web-based RFSoC/DFE inspection tools."
HOMEPAGE = "https://bokeh.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4d2241e774601133cb2c58ae1277f9a5"

DEPENDS += " \
	python3-colorama-native \
	python3-setuptools-git-versioning-native \
	"

PYPI_PACKAGE = "bokeh"

inherit pypi python_setuptools_build_meta

SRC_URI[sha256sum] = "8e7dcacc21d53905581b54328ad2705954f72f2997f99fc332c1de8da53aa3cc"

RDEPENDS:${PN} = " \
	python3-contourpy \
	python3-jinja2 \
	python3-narwhals \
	python3-numpy \
	python3-packaging \
	python3-pandas \
	python3-pillow \
	python3-pyyaml \
	python3-tornado \
	python3-xyzservices \
	"
