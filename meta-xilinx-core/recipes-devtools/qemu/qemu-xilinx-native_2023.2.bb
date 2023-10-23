require qemu-xilinx-2023.2.inc
require qemu-xilinx-native-7.1.inc
require qemu-native-alt.inc

BPN = "qemu-xilinx"

EXTRA_OECONF:append = " --target-list=${@get_qemu_usermode_target_list(d)} --disable-tools --disable-blobs --disable-guest-agent"

PACKAGECONFIG ??= "pie"
