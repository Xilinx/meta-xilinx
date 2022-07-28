SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=v0.2022.x;protocol=https"
SRCREV = "cdb1a7b55c375f5237683a512257e0fc573063a2"

BASEVERSION = "1.1.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    "
