# Setup for MicroBlaze and Zynq architectures
COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE_zynq = "zynq"
COMPATIBLE_MACHINE_zynqmp = "zynqmp"
COMPATIBLE_MACHINE_microblaze = "microblaze"

# Default kernel config fragements for specific machines
KERNEL_FEATURES_append_ml605-qemu-microblazeel = " bsp/ml605-qemu-microblazeel.scc"
KERNEL_FEATURES_append_s3adsp1800-qemu-microblazeeb = " bsp/s3adsp1800-qemu-microblazeeb.scc"
KERNEL_FEATURES_append_kc705-microblazeel = " bsp/kc705-microblazeel/kc705-microblazeel.scc"

# MicroBlaze is a uImage target, but its not called 'uImage' instead it is called 'linux.bin.ub'
python () {
    if d.getVar('KERNEL_IMAGETYPE', True).endswith('.ub'):
        d.setVar('DEPENDS', "%s u-boot-mkimage-native" % d.getVar('DEPENDS', True))
}

