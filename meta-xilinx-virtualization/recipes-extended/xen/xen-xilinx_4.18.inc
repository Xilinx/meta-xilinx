SRCREV = "7cb7aac7f570757b67bcd43aec67e0cda9f58b14"
XEN_URI = "git://github.com/Xilinx/xen.git;protocol=https"
XEN_BRANCH = "xlnx_rebase_4.18"

PV .= "-xilinx+git${SRCPV}"

DEFAULT_PREFERENCE = "+1"