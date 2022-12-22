require qemu-system-native-alt.inc
require qemu-xilinx-2022.1.inc
require qemu-xilinx-native-7.1.inc

PROVIDES = "qemu-system-native"

EXTRA_OECONF:append = " --target-list=${@get_qemu_system_target_list(d)}"

PACKAGECONFIG ??= "fdt alsa kvm gcrypt pie slirp"

PACKAGECONFIG:remove = "${@'kvm' if not os.path.exists('/usr/include/linux/kvm.h') else ''}"

DEPENDS += "pixman-native qemu-xilinx-native bison-native ninja-native meson-native"
DEPENDS += "qemu-xilinx-multiarch-helper-native"

do_install:append() {
    # The following is also installed by qemu-native
    rm -f ${D}${datadir}/qemu/trace-events-all
    rm -rf ${D}${datadir}/qemu/keymaps
    rm -rf ${D}${datadir}/icons
    rm -rf ${D}${includedir}/qemu-plugin.h

    # Install qmp.py to be used with testimage
    install -d ${D}${libdir}/qemu-python/qmp/
    install -D ${S}/python/qemu/qmp/* ${D}${libdir}/qemu-python/qmp/
}
