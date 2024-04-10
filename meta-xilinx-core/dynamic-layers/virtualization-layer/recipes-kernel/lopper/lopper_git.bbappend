SRC_URI = "git://github.com/devicetree-org/lopper.git;branch=v0.2024.x;protocol=https"
SRCREV = "30bed2bbebeae4c190a74a5d6f26f43a62135041"

FILESEXTRAPATHS:prepend := "${THISDIR}/lopper:"

BASEVERSION = "1.1.0"

RDEPENDS:${PN} += " \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "
