# Kernel version and SRCREV correspond to:
LINUX_VERSION = "3.10"
# xilinx-v14.7 tag
SRCREV ?= "efc27505715e64526653f35274717c0fc56491e3"
PR = "r1"

include linux-xlnx.inc

# The MACB driver is non-functional in the 3.10 kernel
KERNEL_FEATURES_append_zynq += "features/xilinx/disable-macb.scc"

