FILESEXTRAPATHS:prepend := "${THISDIR}/linux-xlnx:"

require linux-microblaze.inc

# Zynq default generic KMACHINE
COMPATIBLE_MACHINE:zynq = "zynq"
KMACHINE:zynq = "zynq"

# ZynqMP default generic KMACHINE
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
KMACHINE:zynqmp = "zynqmp"

# MicroBlaze KMACHINEs
KMACHINE:ml605-qemu-microblazeel = "qemumicroblazeel"
KMACHINE:s3adsp1800-qemu-microblazeeb = "qemumicroblazeeb"

# MicroBlaze default generic KMACHINE
KMACHINE:microblaze = "microblaze"
COMPATIBLE_MACHINE:microblaze = "microblaze"

# Default kernel config fragements for specific machines
KERNEL_FEATURES:append:kc705-microblazeel = " bsp/xilinx/kc705-microblazeel-features/kc705-microblazeel-features.scc"

