SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=v0.2024.x;protocol=https"
SRCREV = "9e880fa8bad815f01ca8ec4a3e141e5386f012fd"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.1.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
