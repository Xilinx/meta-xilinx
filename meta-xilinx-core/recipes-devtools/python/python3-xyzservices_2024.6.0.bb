SUMMARY = "Source of XYZ tiles providers"
HOMEPAGE = "https://github.com/geopandas/xyzservices"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a3e440ffacb99f21fa410467c048574"

DEPENDS += "python3-setuptools-scm-native"

SRC_URI[sha256sum] = "58c1bdab4257d2551b9ef91cd48571f77b7c4d2bc45bf5e3c05ac97b3a4d7282"

inherit pypi python_setuptools_build_meta

FILES:${PN} += "${prefix}"

RDEPENDS:${PN} += " \
	python3-core \
	python3-json \
	python3-mercantile \
	python3-netclient \
	python3-requests \
	"
