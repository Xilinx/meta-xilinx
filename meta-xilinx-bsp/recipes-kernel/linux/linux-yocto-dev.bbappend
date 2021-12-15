# MicroBlaze KMACHINEs
KMACHINE:ml605-qemu-microblazeel = "qemumicroblazeel"
KMACHINE:s3adsp1800-qemu-microblazeeb = "qemumicroblazeeb"

# Default kernel config fragements for specific machines
KERNEL_FEATURES:append:kc705-microblazeel = " bsp/xilinx/kc705-microblazeel-features/kc705-microblazeel-features.scc"

