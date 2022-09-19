SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

# x86_64 is needed to build nativesdks
QEMU_TARGETS = "aarch64 arm microblaze microblazeel x86_64"

LIC_FILES_CHKSUM = " \
		file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
		file://COPYING.LIB;endline=24;md5=8c5efda6cf1e1b03dcfd0e6c0d271c7f \
		"
DEPENDS = "glib-2.0 zlib pixman bison-native ninja-native meson-native"

XILINX_QEMU_VERSION ?= "v6.1.0"
BRANCH ?= "xlnx_rel_v2022.2"
SRCREV = "74d70f8008bfbd1566c2eed3496a077a974de0d0"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PV = "${XILINX_QEMU_VERSION}-xilinx-${XILINX_RELEASE_VERSION}+git${SRCPV}"
REPO ?= "gitsm://github.com/Xilinx/qemu.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

SRC_URI:append = " file://0010-configure-Add-pkg-config-handling-for-libgcrypt.patch"

S = "${WORKDIR}/git"

# Disable KVM completely
PACKAGECONFIG:remove = "kvm"
PACKAGECONFIG:append = " fdt sdl gcrypt pie"

PACKAGECONFIG[gcrypt] = "--enable-gcrypt,--disable-gcrypt,libgcrypt,"
PACKAGECONFIG[sdl] = "--enable-sdl,--disable-sdl,libsdl2"
PACKAGECONFIG[pie] = "--enable-pie,--disable-pie,,"

DISABLE_STATIC:pn-${PN} = ""

PTEST_ENABLED = ""

do_install:append() {
	# Prevent QA warnings about installed ${localstatedir}/run
	if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}

FILES:${PN} += "${datadir}/qemu/"
FILES:${PN}:append:class-nativesdk:mingw32 = " ${SDKPATHNATIVE}"
