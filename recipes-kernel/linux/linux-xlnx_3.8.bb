# See include file for common information
include linux-xlnx.inc

PR = "r1"

# Kernel version and SRCREV correspond to: github.com/Xilinx v14.5 tag
LINUX_VERSION = "3.8"
SRCREV = "6a0bedad60e2bca8d9b50bf81b9895e29e31a6d7"

# MicroBlaze patches
SRC_URI_append = " \
		file://microblaze-patches_v3.8.scc \
		"
