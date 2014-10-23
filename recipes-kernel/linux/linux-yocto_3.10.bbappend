
require linux-xilinx-configs.inc
require linux-xilinx-machines.inc

# The MACB driver is non-functional in the 3.10 kernel
KERNEL_FEATURES_append_zynq += "features/xilinx/disable-macb.scc"

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-yocto:"
SRC_URI_append_microblaze += " \
		file://ec2eba55f0c0e74dd39aca14dcc597583cf1eb67.patch \
		file://218a12f1f41f6fdce18d084e5ddd3c6439db0983.patch \
		file://7f15a256b556bf26780d7a0bd03c88914a852022.patch \
		file://99399545d62533b4ae742190b5c6b11f7a5826d9.patch \
		"
