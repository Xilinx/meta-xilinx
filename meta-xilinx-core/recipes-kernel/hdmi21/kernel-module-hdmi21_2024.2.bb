SUMMARY = "Xilinx HDMI 2.1 FMC linux kernel module"
DESCRIPTION = "Out-of-tree HDMI 2.1 FMC kernel modules provider for aarch64 devices"
SECTION = "PETALINUX/modules"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a"

inherit module

SRC_BRANCH = "master"
INHIBIT_PACKAGE_STRIP = "1"

SRC_URI = "git://github.com/Xilinx/hdmi21-modules.git;protocol=https;branch=${SRC_BRANCH}"

SRCREV = "26a1d40723c58783f7aedba028a208ab9410df5f"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.
