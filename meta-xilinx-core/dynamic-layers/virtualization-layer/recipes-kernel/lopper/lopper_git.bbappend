SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=master;protocol=https"
SRCREV = "3c69852c71aba0bf5711b8463fb9ab38345faa42"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

SRC_URI += "file://0001-openamp-xlnx-Fix-typo-in-comparison.patch"

BASEVERSION = "1.2.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
