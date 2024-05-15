SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=v0.2024.x;protocol=https"
SRCREV = "4fb08575157d7712e0cd50e9e9c07620bc9f8b4b"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.1.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
