SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=master;protocol=https"
SRCREV = "d01a1742a626149d956008afe8d4f66a1657f71c"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.1.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
