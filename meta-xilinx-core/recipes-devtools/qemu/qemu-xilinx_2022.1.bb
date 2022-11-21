require qemu-xilinx-2022.1.inc
require recipes-devtools/qemu/qemu.inc
require qemu-xilinx.inc
require qemu-alt.inc

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
