SRC_URI = "git://github.com/Xilinx/lopper.git;branch=xlnx_rel_v2026.1.1;protocol=https"
SRCREV = "126812dce4748f8f251234892b79bdd25e82d7df"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.3.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
