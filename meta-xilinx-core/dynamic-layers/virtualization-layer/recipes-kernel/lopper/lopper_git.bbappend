SRC_URI = "git://github.com/Xilinx/lopper.git;branch=xlnx_rel_v2026.1;protocol=https"
SRCREV = "7daa7d3d51b90ff5c37422af41dd544f4e4f5e14"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.3.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
