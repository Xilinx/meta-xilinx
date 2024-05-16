BBCLASSEXTEND = "nativesdk"

require qemu-xilinx-2024.2.inc
require qemu-8.1.inc
require qemu-xilinx-8.1.inc
require qemu-alt.inc

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH[vardepsexclude] = "MACHINE_ARCH"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH[vardepsexclude] = "MALI_PACKAGE_ARCH"
PACKAGE_ARCH:class-target = "${@bb.utils.contains_any('DEPENDS', 'libepoxy virglrenderer', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"


DEPENDS = "glib-2.0 zlib pixman bison-native ninja-native meson-native"

DEPENDS:append:libc-musl = " libucontext"

CFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'x11', '', '-DEGL_NO_X11=1', d)}"

RDEPENDS:${PN}-common:class-target += "bash"

EXTRA_OECONF:append:class-target = " --target-list=${@get_qemu_target_list(d)}"
EXTRA_OECONF:append:class-target:mipsarcho32 = "${@bb.utils.contains('BBEXTENDCURR', 'multilib', ' --disable-capstone', '', d)}"
EXTRA_OECONF:append:class-nativesdk = " --target-list=${@get_qemu_target_list(d)}"

PACKAGECONFIG ??= " \
    fdt sdl kvm pie slirp gcrypt \
    ${@bb.utils.filter('DISTRO_FEATURES', 'alsa pulseaudio xen', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virglrenderer epoxy', '', d)} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'seccomp', d)} \
"
PACKAGECONFIG:class-nativesdk ??= "fdt sdl kvm pie slirp gcrypt \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'virglrenderer epoxy', '', d)} \
"
# ppc32 hosts are no longer supported in qemu
COMPATIBLE_HOST:powerpc = "null"
