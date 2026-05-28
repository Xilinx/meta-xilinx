SRC_URI = "git://github.com/Xilinx/lopper.git;branch=xlnx_rel_v2026.1;protocol=https"
SRCREV = "c97d6fefe1142497d89a704248c9e7d22b02da2e"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.3.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
