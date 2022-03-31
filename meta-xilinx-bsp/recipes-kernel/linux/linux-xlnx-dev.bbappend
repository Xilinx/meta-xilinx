KERNEL_MODULE_AUTOLOAD:k26 += "usb5744"
KERNEL_MODULE_AUTOLOAD:k26 += "usb2244"

# MicroBlaze BSP fragments
KERNEL_FEATURES:append:kc705-microblazeel = " bsp/xilinx/kc705-microblazeel-features/kc705-microblazeel-features.scc"

