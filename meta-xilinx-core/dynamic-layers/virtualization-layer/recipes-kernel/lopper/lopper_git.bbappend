SRC_URI = "git://github.com/Xilinx/lopper.git;branch=xlnx_rel_v2026.1.1;protocol=https"
SRCREV = "44a55fd38fa7f9996dd1a8afc743af7ffd877777"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.3.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
