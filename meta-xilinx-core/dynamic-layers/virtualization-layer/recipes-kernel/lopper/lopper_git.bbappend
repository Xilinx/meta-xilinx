SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=master;protocol=https"
SRCREV = "0e640aae7b128347d9b72f8f03e995bc19ce06a8"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.1.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    "
