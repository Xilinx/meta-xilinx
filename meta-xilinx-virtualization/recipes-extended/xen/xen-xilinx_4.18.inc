SRCREV = "6c8d45643aad8fcfe0e4b31ae9b36ceeb2bf91c5"
XEN_URI = "git://github.com/Xilinx/xen.git;protocol=https"
XEN_BRANCH = "xlnx_rebase_4.18"

PV .= "-xilinx+git${SRCPV}"

DEFAULT_PREFERENCE = "+1"
