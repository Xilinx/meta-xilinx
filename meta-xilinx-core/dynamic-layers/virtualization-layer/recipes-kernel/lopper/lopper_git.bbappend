SRC_URI = "git://github.com/Xilinx/lopper.git;branch=xlnx_rel_v2026.1;protocol=https"
SRCREV = "26c5ea65248a92cfae7a3c0b0e9c4db02cf5c937"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.3.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
