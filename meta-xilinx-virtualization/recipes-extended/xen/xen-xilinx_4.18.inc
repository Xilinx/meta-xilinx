SRCREV = "3c2e9a28a39bd35fcafe8db1e7d6cba538d61818"
XEN_URI = "git://github.com/Xilinx/xen.git;protocol=https"
XEN_BRANCH = "xlnx_rebase_4.18"

PV .= "-xilinx+git"

DEFAULT_PREFERENCE = "+1"

SRC_URI = " \
    ${XEN_URI};branch=${XEN_BRANCH} \
    file://0001-python-pygrub-pass-DISTUTILS-xen-4.18.patch \
    "
