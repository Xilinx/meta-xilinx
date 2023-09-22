require qemu-xilinx-2022.2.inc
require qemu-xilinx-native-7.1.inc
require qemu-native-alt.inc

BPN = "qemu-xilinx"

# Latest poky has changed the defaults, restore them to something compatible
# with this QEMU.  When we update to QEMU 8.x this won't be necessary.
EXTRA_OECONF:remove = "--disable-download"
EXTRA_OECONF:remove = "--disable-docs"

EXTRA_OECONF:append = "\
    --with-git=/bin/false \
    --with-git-submodules=ignore \
    --meson=meson \
"

EXTRA_OECONF:append = " --target-list=${@get_qemu_usermode_target_list(d)} --disable-tools --disable-blobs --disable-guest-agent"

PACKAGECONFIG ??= "pie"
