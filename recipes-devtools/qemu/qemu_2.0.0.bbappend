QEMU_TARGETS += "microblazeel"

FILESEXTRAPATHS_append := "${THISDIR}/files:"
SRC_URI_append += " \
		file://HACK_zynq_slcr_Bring_SLCR_out_of_reset_in_kernel_state.patch \
		file://net-xilinx_axienet.c-Add-phy-soft-reset-bit-clearing.patch \
		"
