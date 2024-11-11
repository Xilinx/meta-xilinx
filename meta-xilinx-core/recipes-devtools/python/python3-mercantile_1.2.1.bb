SUMMARY = "Web mercator XYZ tile utilities"
HOMEPAGE = "https://github.com/mapbox/mercantile"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=11e1a3a1ec3801b170b9152c135e8f74 \
                    file://docs/license.rst;md5=083a4719b463be5b728fd0e3f47db7e7"

SRC_URI[sha256sum] = "fa3c6db15daffd58454ac198b31887519a19caccee3f9d63d17ae7ff61b3b56b"

inherit pypi setuptools3

RDEPENDS:${PN} += " \
	python3-click \
	python3-core \
	python3-json \
	python3-logging \
	"
