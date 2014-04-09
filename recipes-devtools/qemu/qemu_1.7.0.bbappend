QEMU_TARGETS += "microblazeel"

FILESEXTRAPATHS_append := "${THISDIR}/files:"
SRC_URI_append += " \
		file://HACK_target-arm_Harcode_the_SCU_offset.patch \
		file://HACK_zynq_slcr_Bring_SLCR_out_of_reset_in_kernel_state.patch \
		file://qom_object_c_Split_out_object_and_class_caches.patch \
		file://hw-net-xilinx_axienet.c-Add-phy-soft-reset-bit-clear.patch \
		"
