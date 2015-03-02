# Setup for MicroBlaze and Zynq architectures
COMPATIBLE_MACHINE_zynq = "zynq"
COMPATIBLE_MACHINE_microblaze = "microblaze"

# Default kernel config fragements for specific machines
KERNEL_FEATURES_append_qemumicroblaze += "bsp/qemumicroblaze/qemumicroblaze.scc"
KERNEL_FEATURES_append_kc705-trd-microblazeel += "bsp/kc705-trd-microblazeel/kc705-trd-microblazeel.scc"

# MicroBlaze is a uImage target, but its not called 'uImage'
DEPENDS_append_microblaze += "u-boot-mkimage-native"

