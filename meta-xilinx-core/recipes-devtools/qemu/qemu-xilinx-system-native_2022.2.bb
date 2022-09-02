require qemu-xilinx-native.inc

PROVIDES = "qemu-system-native"

EXTRA_OECONF:append = " --target-list=${@get_qemu_system_target_list(d)}"

PACKAGECONFIG ??= "fdt alsa kvm pie"

PACKAGECONFIG:remove = "${@'kvm' if not os.path.exists('/usr/include/linux/kvm.h') else ''}"

DEPENDS += "pixman-native qemu-xilinx-native bison-native ninja-native meson-native"

do_install:append() {
    # The following is also installed by qemu-native
    rm -f ${D}${datadir}/qemu/trace-events-all
    rm -rf ${D}${datadir}/qemu/keymaps
    rm -rf ${D}${datadir}/icons
    rm -rf ${D}${includedir}/qemu-plugin.h
}

