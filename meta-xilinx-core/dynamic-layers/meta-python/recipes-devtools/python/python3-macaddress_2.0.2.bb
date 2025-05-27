SUMMARY = "Python 3.3+'s macaddress for Python 2.6, 2.7, 3.2."
HOMEPAGE = "https://github.com/mentalisttraceur/python-macaddress"
LICENSE = "BSD-0-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ea2eb804531932406bb4957581a4b0f4"

SRC_URI[sha256sum] = "1400ccdc28d747102d57ae61e5b78d8985872930810ceb8860cd49abd1e1fa37"

inherit pypi setuptools3

BBCLASSEXTEND = "native nativesdk"
