SUMMARY = "SDK Helper scripts for executing a multi-arch instance of \
AMD QEMU"
DESCRIPTION = "SDK-side wrapper script that launches the heterogeneous \
multi-arch instance of the AMD QEMU fork (APU/RPU/PMU/MicroBlaze) from \
a single command line, for SDK users."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PROVIDES += "nativesdk-flashstrip"

inherit setuptools3 nativesdk

require qemu-xilinx-multiarch-helper.inc

S = "${WORKDIR}/git"
B = "${S}"

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

RDEPENDS:${PN} += "nativesdk-qemu-xilinx-common nativesdk-wic"
RPROVIDES:${PN} += "nativesdk-flashstrip"
