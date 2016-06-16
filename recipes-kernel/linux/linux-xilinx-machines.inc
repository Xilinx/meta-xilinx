# Setup for MicroBlaze and Zynq architectures
COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE_zynq = "zynq"
COMPATIBLE_MACHINE_microblaze = "microblaze"

# Default kernel config fragements for specific machines
KERNEL_FEATURES_append_qemumicroblaze += "bsp/qemumicroblaze/qemumicroblaze.scc"
KERNEL_FEATURES_append_qemumicroblaze-s3adsp1800 += "bsp/qemumicroblaze-s3adsp1800/qemumicroblaze-s3adsp1800.scc"
KERNEL_FEATURES_append_kc705-trd-microblazeel += "bsp/kc705-trd-microblazeel/kc705-trd-microblazeel.scc"

# MicroBlaze is a uImage target, but its not called 'uImage' instead it is called 'linux.bin.ub'
python () {
    if d.getVar('KERNEL_IMAGETYPE', True).endswith('.ub'):
        d.setVar('DEPENDS', "%s u-boot-mkimage-native" % d.getVar('DEPENDS', True))
}

