SRCREV = "a4754372819eb69acb658fc013ad35c4d55bf9a5"
XEN_URI = "git://github.com/Xilinx/xen.git;protocol=https"
XEN_BRANCH = "xlnx_rebase_4.18"

PV .= "-xilinx+git${SRCPV}"

DEFAULT_PREFERENCE = "+1"

SRC_URI = " \
    ${XEN_URI};branch=${XEN_BRANCH} \
    file://0001-python-pygrub-pass-DISTUTILS-xen-4.18.patch \
    "
