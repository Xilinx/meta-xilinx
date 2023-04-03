SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

# x86_64 is needed to build nativesdks
QEMU_TARGETS = "aarch64 arm microblaze microblazeel x86_64"

LIC_FILES_CHKSUM = " \
		file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
		file://COPYING.LIB;endline=24;md5=8c5efda6cf1e1b03dcfd0e6c0d271c7f \
		"
DEPENDS = "glib-2.0 zlib pixman bison-native ninja-native meson-native"

FILESEXTRAPATHS:prepend := "${THISDIR}/qemu-xilinx-7.1.0:"

PV = "${XILINX_QEMU_VERSION}-xilinx-v${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or ''}+git${SRCPV}"
REPO ?= "gitsm://github.com/Xilinx/qemu.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

FILESEXTRAPATHS:append := ":${COREBASE}/meta/recipes-devtools/qemu/qemu"

SRC_URI += "\
           file://powerpc_rom.bin \
           file://run-ptest \
           file://0001-qemu-Add-addition-environment-space-to-boot-loader-q.patch \
           file://0002-chardev-connect-socket-to-a-spawned-command.patch \
           file://0003-apic-fixup-fallthrough-to-PIC.patch \
           file://0004-configure-Add-pkg-config-handling-for-libgcrypt.patch \
           file://0005-qemu-Do-not-include-file-if-not-exists.patch \
           file://0006-qemu-Add-some-user-space-mmap-tweaks-to-address-musl.patch \
           file://0007-qemu-Determinism-fixes.patch \
           file://0008-tests-meson.build-use-relative-path-to-refer-to-file.patch \
           file://0009-Define-MAP_SYNC-and-MAP_SHARED_VALIDATE-on-needed-li.patch \
           file://0010-hw-pvrdma-Protect-against-buggy-or-malicious-guest-d.patch \
           file://0001-net-tulip-Restrict-DMA-engine-to-memories.patch \
           file://arm-cpreg-fix.patch \
           file://CVE-2022-3165.patch \
           file://qemu-guest-agent.init \
           file://qemu-guest-agent.udev \
           "

S = "${WORKDIR}/git"

PACKAGECONFIG ??= " \
    fdt sdl kvm gcrypt pie slirp \
    ${@bb.utils.filter('DISTRO_FEATURES', 'alsa xen', d)} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'seccomp', d)} \
"
PACKAGECONFIG:class-nativesdk ??= "fdt sdl kvm gcrypt pie slirp"

DISABLE_STATIC:pn-${PN} = ""
