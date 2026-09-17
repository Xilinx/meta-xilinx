SRCREV = "9fa34377dd6f64e13ed349813ac9039295f224a7"
XEN_URI = "git://github.com/Xilinx/xen.git;protocol=https"
XEN_BRANCH = "xlnx_rebase_4.21"

PV .= "-xilinx+git"

DEFAULT_PREFERENCE = "+1"

SRC_URI = " \
    ${XEN_URI};branch=${XEN_BRANCH} \
    file://0001-python-pygrub-pass-DISTUTILS-xen-4.19.patch \
    file://0001-libxl_nocpuid-fix-build-error.patch \
    file://0001-tools-libxl-Fix-build-with-NOCPUID-and-json-c.patch \
    file://0001-ARM-Drop-ThumbEE-support.patch \
    "

include cve-exclusion-4.21.inc
