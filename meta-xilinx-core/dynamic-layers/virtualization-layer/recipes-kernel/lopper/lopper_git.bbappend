SRC_URI = "git://github.com/Xilinx/lopper.git;branch=xlnx_rel_v2026.1.1;protocol=https"
SRCREV = "da9dea6b4b2b9e32730aa9722a62893da46e9825"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.3.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
