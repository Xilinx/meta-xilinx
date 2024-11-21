SUMMARY = "Interactive plots and applications in the browser from Python"
HOMEPAGE = "https://bokeh.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4d2241e774601133cb2c58ae1277f9a5"

DEPENDS += " \
	python3-colorama-native \
	python3-setuptools-git-versioning-native \
	"

SRC_URI[sha256sum] = "21dbe86842b24d83c73a1aef2de346a1a7c11c437015d6e9d180a1637e6e8197"

inherit pypi python_setuptools_build_meta

RDEPENDS:${PN} = " \
	python3-colorama \
	python3-contourpy \
	python3-jinja2 \
	python3-numpy \
	python3-packaging \
	python3-pandas \
	python3-pillow \
	python3-pyyaml \
	python3-tornado \
	python3-typing-extensions \
	python3-xyzservices \
	"
