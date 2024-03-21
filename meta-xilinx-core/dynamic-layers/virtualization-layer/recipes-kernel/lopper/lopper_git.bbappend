SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=v0.2024.x;protocol=https"
SRCREV = "a85a86eba10c7cd0f8aa4c99aa647ae8bdd72d70"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.1.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
