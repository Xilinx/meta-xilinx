SRC_URI = "git://github.com/Xilinx/lopper.git;branch=xlnx_rel_v2026.1;protocol=https"
SRCREV = "1795b1ead42da6ecfeda135d253df37c81affca2"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.3.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
