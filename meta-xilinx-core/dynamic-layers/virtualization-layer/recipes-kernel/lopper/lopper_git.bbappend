SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=master;protocol=https"
SRCREV = "146b2e86c888ee405cceeaccc290bd24f8ff2d03"

BASEVERSION = "1.1.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    "
