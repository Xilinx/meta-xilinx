SUMMARY = "Use git repo data for building a version number according PEP-440"
HOMEPAGE = "https://setuptools-git-versioning.readthedocs.io"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=92e79e3a844e66731724600f3ac9c0d8"

SRC_URI[sha256sum] = "85b5fbe7bda8e9c24bbd9e587a9d4b91129417f4dd3e11e3c0d5f3f835fc4d4d"

inherit pypi setuptools3

RDEPENDS:${PN} += " \
	python3-core \
	python3-datetime \
	python3-logging \
	python3-packaging \
	python3-pprint \
	python3-setuptools \
	python3-tomllib \
	"

BBCLASSEXTEND += "native"
