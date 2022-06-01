FILESEXTRAPATHS:prepend := "${COREBASE}/meta/recipes-devtools/qemu/qemu-helper:"

# provide it, to replace the existing
PROVIDES = "qemu-helper-native"
PR = "r1"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/tunctl.c;endline=4;md5=ff3a09996bc5fff6bc5d4e0b4c28f999"

SRC_URI = "\
    file://tunctl.c \
    "

S = "${WORKDIR}"

inherit native

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} -Wall tunctl.c -o tunctl
}
# replace qemu with qemu-xilinx
DEPENDS:remove = "qemu-system-native"
DEPENDS:append = " \
		qemu-xilinx-system-native \
		qemu-xilinx-multiarch-helper-native \
		"

RDEPENDS:${PN}:remove = "qemu-system-native"
RDEPENDS:${PN}:append = " qemu-xilinx-system-native"

do_install() {
        install -d ${STAGING_BINDIR_NATIVE}
	install tunctl ${STAGING_BINDIR_NATIVE}

}
addtask addto_recipe_sysroot after do_populate_sysroot before do_build
