require qemu-xilinx-2022.1.inc
require recipes-devtools/qemu/qemu.inc
require qemu-xilinx-7.1.inc
require qemu-alt.inc

# Links to libmali-xlnx, so it becomes MACHINE_ARCH specific
DEFAULT_PACKAGE_ARCH := "${PACKAGE_ARCH}"
MALI_PACKAGE_ARCH[vardepsexclude] = "MACHINE_ARCH"
MALI_PACKAGE_ARCH = "${@'${MACHINE_ARCH}' if d.getVar('PREFERRED_PROVIDER_virtual/libgles1') == 'libmali-xlnx' else '${DEFAULT_PACKAGE_ARCH}'}"
PACKAGE_ARCH[vardepsexclude] = "MALI_PACKAGE_ARCH"
PACKAGE_ARCH:class-target = "${@bb.utils.contains_any('DEPENDS', 'libepoxy virglrenderer', '${MALI_PACKAGE_ARCH}', '${DEFAULT_PACKAGE_ARCH}', d)}"

BBCLASSEXTEND = "nativesdk"

RDEPENDS:${PN}:class-target += "bash"

PROVIDES:class-nativesdk = "nativesdk-qemu"
RPROVIDES:${PN}:class-nativesdk = "nativesdk-qemu"

EXTRA_OECONF:append:class-target = " --target-list=${@get_qemu_target_list(d)}"
EXTRA_OECONF:append:class-nativesdk = " --target-list=${@get_qemu_target_list(d)}"
EXTRA_OECONF:append:class-target:mipsarcho32 = "${@bb.utils.contains('BBEXTENDCURR', 'multilib', ' --disable-capstone', '', d)}"

do_install:append:class-nativesdk() {
    ${@bb.utils.contains('PACKAGECONFIG', 'gtk+', 'make_qemu_wrapper', '', d)}
}
