SRCREV = "ab14336ebd95682f43e99430743217b09af55200"
XEN_URI = "git://github.com/Xilinx/xen.git;protocol=https"
XEN_BRANCH = "xlnx_rebase_4.21"

PV .= "-xilinx+git"

DEFAULT_PREFERENCE = "+1"

SRC_URI = " \
    ${XEN_URI};branch=${XEN_BRANCH} \
    file://0001-python-pygrub-pass-DISTUTILS-xen-4.19.patch \
    "
