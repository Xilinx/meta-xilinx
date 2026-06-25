SUMMARY = "Helper scripts for executing a multi-arch instance of \
Xilinx QEMU"
DESCRIPTION = "Native build-host wrapper script that launches the \
heterogeneous multi-arch instance of the AMD QEMU fork \
(APU/RPU/PMU/MicroBlaze) from a single command line."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# This is a typo in the older version of the package name
PROVIDES += "flashstrip-native"

inherit setuptools3 native

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

require qemu-xilinx-multiarch-helper.inc

S = "${WORKDIR}/git"
B = "${S}"

SYSROOT_DIRS += "${bindir}/qemu-xilinx"

do_install:append() {
	install -Dm 0755 ${S}/qemu-system-amd-fpga-multiarch ${D}${bindir}/qemu-system-amd-fpga-multiarch
	install -Dm 0755 ${S}/flash_stripe.py ${D}${bindir}/flash_stripe.py
	ln -s flash_stripe.py ${D}${bindir}/flash_stripe
	ln -s flash_stripe.py ${D}${bindir}/flash_unstripe
	ln -s flash_stripe.py ${D}${bindir}/flash_stripe_bw
	ln -s flash_stripe.py ${D}${bindir}/flash_unstripe_bw
	# Match the original typo name for compatibility
	ln -s flash_stripe.py ${D}${bindir}/flash_strip
	ln -s flash_stripe.py ${D}${bindir}/flash_unstrip
	ln -s flash_stripe.py ${D}${bindir}/flash_strip_bw
	ln -s flash_stripe.py ${D}${bindir}/flash_unstrip_bw
}

