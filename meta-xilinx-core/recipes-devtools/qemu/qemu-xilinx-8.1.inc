SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

# This qemu fork is NOT compatible with running on a 32-bit system
# See: https://github.com/Xilinx/qemu/issues/35
COMPATIBLE_HOST:arm = "null"

# x86_64 is needed to build nativesdks
QEMU_TARGETS = "aarch64 arm microblaze microblazeel riscv32 x86_64"

BRANCH = "xlnx_rel_v2024.2"
SRCREV = "01482fa113dcbfa785feb7d513df50d15ec4c5df"

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=8c5efda6cf1e1b03dcfd0e6c0d271c7f"

FILESEXTRAPATHS:prepend := "${THISDIR}/qemu-xilinx-8.1.0:"

PV .= "+git"
REPO ?= "gitsm://github.com/Xilinx/qemu.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG};name=qemu"
SRCREV_qemu = "${SRCREV}"
SRCREV_FORMAT = "qemu"

FILESEXTRAPATHS:append := ":${THISDIR}/qemu-8.1"

# Deal with the broken --disable-download
SRCREV_berkeley-softfloat-3 = "b64af41c3276f97f0e181920400ee056b9c88037"
SRCREV_berkeley-testfloat-3 = "40619cbb3bf32872df8c53cc457039229428a263"
SRCREV_dtc = "b6910bec11614980a21e46fbccc35934b671bd81"
SRCREV_keycodemapdb = "f5772a62ec52591ff6870b7e8ef32482371f22c6"
SRCREV_libvfio-user = "0b28d205572c80b568a1003db2c8f37ca333e4d7"
SRCREV_slirp = "26be815b86e8d49add8c9a8b320239b9594ff03d"
SRC_URI += "\
           git://gitlab.com/qemu-project/berkeley-softfloat-3;protocol=https;nobranch=1;destsuffix=git/subprojects/berkeley-softfloat-3;name=berkeley-softfloat-3 \
           git://gitlab.com/qemu-project/berkeley-testfloat-3;protocol=https;nobranch=1;destsuffix=git/subprojects/berkeley-testfloat-3;name=berkeley-testfloat-3 \
           git://gitlab.com/qemu-project/dtc.git;protocol=https;nobranch=1;destsuffix=git/subprojects/dtc;name=dtc \
           git://gitlab.com/qemu-project/keycodemapdb.git;protocol=https;nobranch=1;destsuffix=git/subprojects/keycodemapdb;name=keycodemapdb \
           git://gitlab.com/qemu-project/libvfio-user.git;protocol=https;nobranch=1;destsuffix=git/subprojects/libvfio-user;name=libvfio-user \
           git://gitlab.freedesktop.org/slirp/libslirp;protocol=https;nobranch=1;destsuffix=git/subprojects/slirp;name=slirp \
"

# Configure meson for disable-download
do_configure:prepend() {
	cp ${S}/subprojects/packagefiles/berkeley-softfloat-3/* ${S}/subprojects/berkeley-softfloat-3/.
	cp ${S}/subprojects/packagefiles/berkeley-testfloat-3/* ${S}/subprojects/berkeley-testfloat-3/.
}


# Keep this in sync with the main YP QEMU integration
SRC_URI += "\
           file://powerpc_rom.bin \
           file://run-ptest \
           file://0001-qemu-Add-addition-environment-space-to-boot-loader-q.patch \
           file://0003-apic-fixup-fallthrough-to-PIC.patch \
           file://0004-configure-Add-pkg-config-handling-for-libgcrypt.patch \
           file://0005-qemu-Do-not-include-file-if-not-exists.patch \
           file://0006-qemu-Add-some-user-space-mmap-tweaks-to-address-musl.patch \
           file://0007-qemu-Determinism-fixes.patch \
           file://0008-tests-meson.build-use-relative-path-to-refer-to-file.patch \
           file://0009-Define-MAP_SYNC-and-MAP_SHARED_VALIDATE-on-needed-li.patch \
           file://0010-hw-pvrdma-Protect-against-buggy-or-malicious-guest-d.patch \
           file://0002-linux-user-Replace-use-of-lfs64-related-functions-an.patch \
           file://fixedmeson.patch \
           file://no-pip.patch \
           file://qemu-guest-agent.init \
           file://qemu-guest-agent.udev \
           "

# Patch doesn't apply to 8.1.0
#           file://fixmips.patch 

# Additional patches to match Scarthgap, which requires a slightly newer qmp interface
SRC_URI += "\
           file://0001-python-rename-QEMUMonitorProtocol.cmd-to-cmd_raw.patch \
           file://0002-python-qemu-rename-command-to-cmd.patch \
           "


S = "${WORKDIR}/git"

# Based on qemu settings in poky/meta/conf/distro/include/no-static-libs.inc
DISABLE_STATIC:pn-qemu-xilinx = ""
DISABLE_STATIC:pn-qemu-xilinx-native = ""
DISABLE_STATIC:pn-nativesdk-qemu-xilinx = ""
DISABLE_STATIC:pn-qemu-xilinx-system-native = ""

