# AMD specific fork of Lopper.
# Integration should be kept in sync with meta-virtualization.
SUMMARY = "Device Tree Lopper"
DESCRIPTION = "Tool for manipulation of system device tree files"
LICENSE = "BSD-3-Clause"
SECTION = "bootloader"

SRC_URI = "git://github.com/Xilinx/lopper.git;branch=xlnx_rel_v2026.1;protocol=https"
SRCREV = "05dc7e4bf359b60f1e6f7ed7074740afd7955a63"

BASEVERSION = "1.3.0"
PV = "v${BASEVERSION}+amd+git"

PYPA_WHEEL = "${PIP_INSTALL_DIST_PATH}/${BPN}-${BASEVERSION}-*.whl"

LIC_FILES_CHKSUM = "file://LICENSE.md;md5=8e5f5f691f01c9fdfa7a7f2d535be619"

RDEPENDS:${PN} = " \
    python3-core \
    python3-dtc \
    python3-humanfriendly \
    python3-ruamel-yaml \
    python3-anytree \
    python3-six \
    python3-pyyaml \
    python3-packaging \
    "

inherit setuptools3

INHIBIT_PACKAGE_STRIP = "1"

do_install:append() {
        # we have to remove the vendor'd libfdt, since an attempt to strip it
        # will be made, and it will fail in a cross environment.
        rm -rf ${D}/${PYTHON_SITEPACKAGES_DIR}/${BPN}/vendor
}

BBCLASSEXTEND = "native nativesdk"

