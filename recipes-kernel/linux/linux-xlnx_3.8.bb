# See include file for common information
include linux-xlnx.inc

PR = "r1"

# Kernel version and SRCREV correspond to: github.com/Xilinx v14.5 tag
LINUX_VERSION = "3.8"
SRCREV = "6a0bedad60e2bca8d9b50bf81b9895e29e31a6d7"

# MicroBlaze patches
SRC_URI_append_microblaze = " \
		file://microblaze_Do_not_use_r6_in_head.S.patch \
		file://microblaze_Fix_free_init_pages_function.patch \
		file://microblaze_Fix_initrd_support.patch \
		file://arch_microblaze_Add_linux.bin.ub_target.patch \
		"
