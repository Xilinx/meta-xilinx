SRC_URI = "git://github.com/Xilinx/lopper.git;branch=xlnx_rel_v2026.1.1;protocol=https"
SRCREV = "d0a62d8e452ad67b7bac27bbec8454af6f93dd74"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.3.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
