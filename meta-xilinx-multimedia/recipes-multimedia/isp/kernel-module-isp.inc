SUMMARY = "Xilinx Versal-2ve-2vm ISP linux kernel module"
DESCRIPTION = "Versal-2ve-2vm Image Signal Processor (ISP) out-of-tree kernel modules provider for aarch64 devices"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

REPO = "git://github.com/Xilinx/isp-modules.git;protocol=https"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal-2ve-2vm = "versal-2ve-2vm"

S = "${WORKDIR}/git"

BRANCHARG = "${@['nobranch=1', 'branch=${SRC_BRANCH}'][d.getVar('SRC_BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.
inherit module

EXTRA_OEMAKE += "O=${STAGING_KERNEL_BUILDDIR}"

RDEPENDS:${PN} += "isp-firmware"

INHIBIT_PACKAGE_STRIP = "1"
