SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

CVE_PRODUCT = "qemu"

# This qemu fork is NOT compatible with running on a 32-bit system
# See: https://github.com/Xilinx/qemu/issues/35
COMPATIBLE_HOST:arm = "null"

# x86_64 is needed to build nativesdks
QEMU_TARGETS = "aarch64 arm microblaze microblazeel riscv32 riscv64 x86_64"

BRANCH = "master"
SRCREV = "488775712d92e26f0bae2af8993a2f073371004b"

# qemu:qemu-native:qemu-system-native https://nvd.nist.gov/vuln/detail/CVE-2019-12067
CVE_STATUS[CVE-2019-12067] = "upstream-wontfix: \
There was a proposed patch but rejected by upstream qemu. It is unclear if the issue can \
still be reproduced or where exactly any bug is. \
Xilinx QEMU does not support the use case of launching 'production' servers subject to \
denial of service attacks."

# qemu:qemu-native:qemu-system-native https://nvd.nist.gov/vuln/detail/CVE-2021-20255
CVE_STATUS[CVE-2021-20255] = "upstream-wontfix: \
There was a proposed patch https://lists.gnu.org/archive/html/qemu-devel/2021-02/msg06098.html \
qemu maintainers say the patch is incorrect and should not be applied \
The issue is of low impact, at worst sitting in an infinite loop rather than exploitable. \
Xilinx QEMU does not support the use case of launching 'production' servers subject to \
denial of service attacks."

# qemu-xilinx does not support the use case of launching "production" servers subject to denial of service attacks
CVE_STATUS[CVE-2024-7730]  = "upstream-wontfix: Xilinx QEMU does not support the use case of launching 'production' servers subject to denial of service attacks"
CVE_STATUS[CVE-2024-8354]  = "upstream-wontfix: Xilinx QEMU does not support the use case of launching 'production' servers subject to denial of service attacks"

# qemu-xilinx does not enable the LSI53C895A SCSI Host Bus Adapter
CVE_STATUS[CVE-2024-6519]  = "not-applicable-config: We do not enable the LSI53C895A SCSI Host Bus Adapter"

# qemu-xilinx does not support virtualization using hyper-v
CVE_STATUS[CVE-2026-3842]  = "upstream-wontfix: Xilinx models in QEMU do not support Hyper-V virtualization"

# Patch has already been backported
CVE_STATUS[CVE-2026-48914] = "fixed-version: Fixed in Xilinx QEMU version 10.2.4"

LIC_FILES_CHKSUM = "file://COPYING;md5=a3b50d8b88dcc0eb3d7d39b760b9e821 \
                    file://COPYING.LIB;endline=24;md5=8a8178c06478747a771588adec965232 \
                    "

FILESEXTRAPATHS:prepend := "${THISDIR}/qemu-xilinx:"

PV .= "+git"
REPO ?= "gitsm://github.com/Xilinx/qemu.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG};name=qemu"
SRCREV_qemu = "${SRCREV}"
SRCREV_FORMAT = "qemu"

FILESEXTRAPATHS:append := ":${THISDIR}/qemu"

# Deal with the broken --disable-download
SRCREV_berkeley-softfloat-3 = "b64af41c3276f97f0e181920400ee056b9c88037"
SRCREV_berkeley-testfloat-3 = "40619cbb3bf32872df8c53cc457039229428a263"
SRCREV_dtc = "b6910bec11614980a21e46fbccc35934b671bd81"
SRCREV_keycodemapdb = "f5772a62ec52591ff6870b7e8ef32482371f22c6"
SRCREV_libvfio-user = "0b28d205572c80b568a1003db2c8f37ca333e4d7"
SRCREV_slirp = "26be815b86e8d49add8c9a8b320239b9594ff03d"
SRC_URI += "\
           git://gitlab.com/qemu-project/berkeley-softfloat-3.git;protocol=https;nobranch=1;destsuffix=${BP}/subprojects/berkeley-softfloat-3;name=berkeley-softfloat-3 \
           git://gitlab.com/qemu-project/berkeley-testfloat-3.git;protocol=https;nobranch=1;destsuffix=${BP}/subprojects/berkeley-testfloat-3;name=berkeley-testfloat-3 \
           git://gitlab.com/qemu-project/dtc.git;protocol=https;nobranch=1;destsuffix=${BP}/subprojects/dtc;name=dtc \
           git://gitlab.com/qemu-project/keycodemapdb.git;protocol=https;nobranch=1;destsuffix=${BP}/subprojects/keycodemapdb;name=keycodemapdb \
           git://gitlab.com/qemu-project/libvfio-user.git;protocol=https;nobranch=1;destsuffix=${BP}/subprojects/libvfio-user;name=libvfio-user \
           git://gitlab.freedesktop.org/slirp/libslirp.git;protocol=https;nobranch=1;destsuffix=${BP}/subprojects/slirp;name=slirp \
"

# Configure meson for disable-download
do_configure:prepend() {
	cp ${S}/subprojects/packagefiles/berkeley-softfloat-3/* ${S}/subprojects/berkeley-softfloat-3/.
	cp ${S}/subprojects/packagefiles/berkeley-testfloat-3/* ${S}/subprojects/berkeley-testfloat-3/.
}


# Keep this in sync with the main OE-Core QEMU integration
SRC_URI += "\
           file://powerpc_rom.bin \
           file://run-ptest \
           file://fix-strerrorname_np.patch \
           file://0001-qemu-Add-addition-environment-space-to-boot-loader-q.patch \
           file://0002-apic-fixup-fallthrough-to-PIC.patch \
           file://0004-qemu-Do-not-include-file-if-not-exists.patch \
           file://0006-qemu-Determinism-fixes.patch \
           file://0007-tests-meson.build-use-relative-path-to-refer-to-file.patch \
           file://0008-Define-MAP_SYNC-and-MAP_SHARED_VALIDATE-on-needed-li.patch \
           file://0010-configure-lookup-meson-exutable-from-PATH.patch \
           file://0011-qemu-Ensure-pip-and-the-python-venv-aren-t-used-for-.patch \
           file://0001-accel-tcg-Fix-iotlb_to_section-for-different-Address.patch \
           file://qemu-guest-agent.init \
           file://qemu-guest-agent.udev \
           "

# Based on qemu settings in openembedded-core/meta/conf/distro/include/no-static-libs.inc
DISABLE_STATIC:pn-qemu-xilinx = ""
DISABLE_STATIC:pn-qemu-xilinx-native = ""
DISABLE_STATIC:pn-nativesdk-qemu-xilinx = ""
DISABLE_STATIC:pn-qemu-xilinx-system-native = ""

