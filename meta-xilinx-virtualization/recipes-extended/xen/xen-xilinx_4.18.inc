SRCREV = "830f997215a495b04b0e00f816c869f722855272"
XEN_URI = "git://github.com/Xilinx/xen.git;protocol=https"
XEN_BRANCH = "xlnx_rebase_4.18"

PV .= "-xilinx+git${SRCPV}"

DEFAULT_PREFERENCE = "+1"
