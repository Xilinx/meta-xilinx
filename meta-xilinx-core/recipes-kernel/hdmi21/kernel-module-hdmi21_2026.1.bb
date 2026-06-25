SUMMARY = "Xilinx HDMI 2.1 FMC linux kernel module"
DESCRIPTION = "Out-of-tree HDMI 2.1 FMC kernel modules provider for aarch64 devices"
SECTION = "kernel/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

inherit module

SRC_BRANCH = "master"
INHIBIT_PACKAGE_STRIP = "1"

SRC_URI = "git://github.com/Xilinx/hdmi21-modules.git;protocol=https;branch=${SRC_BRANCH}"

SRCREV = "063e5430bc9594609c54d2cd19bce2b9d69237cf"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.
