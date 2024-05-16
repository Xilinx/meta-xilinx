BPN = "qemu-xilinx"

DEPENDS += "glib-2.0-native zlib-native"

require qemu-xilinx-native-8.1.inc
require qemu-native-alt.inc

EXTRA_OECONF:append = " --target-list=${@get_qemu_usermode_target_list(d)} --disable-tools --disable-install-blobs --disable-guest-agent"

PACKAGECONFIG ??= "pie"
