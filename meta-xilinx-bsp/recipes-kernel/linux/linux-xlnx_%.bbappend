FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://linux-xlnx-bsp-kmeta;type=kmeta;name=linux-xlnx-bsp-kmeta;destsuffix=linux-xlnx-bsp-kmeta \
    "

# MicroBlaze BSP fragments
KERNEL_FEATURES:append:kc705-microblazeel = " bsp/xilinx/kc705-microblazeel-features/kc705-microblazeel-features.scc"
KERNEL_FEATURES:append:ac701-microblazeel = " bsp/ac701-microblazeel/ac701-microblazeel.scc"
KERNEL_FEATURES:append:vcu118-microblazeel = " bsp/vcu118-microblazeel/vcu118-microblazeel.scc"
