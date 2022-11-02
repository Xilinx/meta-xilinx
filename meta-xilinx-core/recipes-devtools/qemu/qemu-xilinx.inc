SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

# x86_64 is needed to build nativesdks
QEMU_TARGETS = "aarch64 arm microblaze microblazeel x86_64"

LIC_FILES_CHKSUM = " \
		file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
		file://COPYING.LIB;endline=24;md5=8c5efda6cf1e1b03dcfd0e6c0d271c7f \
		"
DEPENDS = "glib-2.0 zlib pixman bison-native ninja-native meson-native"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PV = "${XILINX_QEMU_VERSION}-xilinx-v${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or ''}+git${SRCPV}"
REPO ?= "gitsm://github.com/Xilinx/qemu.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

SRC_URI:append = " file://0010-configure-Add-pkg-config-handling-for-libgcrypt.patch"

S = "${WORKDIR}/git"

# Disable KVM completely
PACKAGECONFIG:remove = "kvm"
PACKAGECONFIG:append = " fdt sdl gcrypt pie slirp"


# Remove when version is in sync with core oe-core
PACKAGECONFIG:remove:class-nativesdk= "pulsedio"
PACKAGECONFIG:remove:class-native = "pulsedio"
PACKAGECONFIG[alsa] = "--audio-drv-list=alsa,,alsa-lib"
PACKAGECONFIG[pulsedio] = "--audio-drv-list=pa,,pulseaudio"
PACKAGECONFIG[selinux] = ""
PACKAGECONFIG[slirp] = "--enable-slirp=git,--disable-slirp,,"

DISABLE_STATIC:pn-${PN} = ""

PTEST_ENABLED = ""

do_install:append() {
	# Prevent QA warnings about installed ${localstatedir}/run
	if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}

FILES:${PN} += "${datadir}/qemu/"
FILES:${PN}:append:class-nativesdk:mingw32 = " ${SDKPATHNATIVE}"
