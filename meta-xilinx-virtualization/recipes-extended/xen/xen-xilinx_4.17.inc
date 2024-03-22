SRCREV = "38eebc6e5c6f7aa9180672a56d33217bf1ef1ca6"
XEN_URI = "git://github.com/Xilinx/xen.git;protocol=https"
XEN_BRANCH = "xlnx_rebase_4.17"

PV .= "-xilinx+git${SRCPV}"

DEFAULT_PREFERENCE = "+1"
