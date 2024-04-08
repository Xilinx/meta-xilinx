SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=v0.2024.x;protocol=https"
SRCREV = "4672dd2c63660b327970cb6e0c3b20169e73e04b"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.1.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
