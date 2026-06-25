DESCRIPTION = "Reference applications illustrating SDFEC driver features and usage."
SUMMARY = "The example applications are: \
             1. sdfec-demo             - Demonstrates basic SDFEC functionality \
             2. sdfec-interrupts       - Demonstrates interrupt handling \
             3. sdfec-multi-ldpc-codes - Demonstrates multiple LDPC code support \
           Usage: \
             sudo <application_name> \
           Example: \
             sudo sdfec-demo \
             sudo sdfec-interrupts \
             sudo sdfec-multi-ldpc-codes"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/Xilinx/linux-examples.git;protocol=https;branch=xlnx_rel_v2026.1"
SRCREV = "a7895d0f8cb968387967ae7e6a107cf4eaf9175c"

inherit features_check

REQUIRED_MACHINE_FEATURES = "rfsoc"

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git/sd-fec-1.1"

DEPENDS += "libgpiod"

TARGETS_APPS ?= "sdfec-demo sdfec-interrupts sdfec-multi-ldpc-codes"

do_compile() {
	for app_name in ${TARGETS_APPS}; do
		oe_runmake -C ${S}/$app_name/files
	done
}

do_install() {
	install -d ${D}${bindir}
	for app_name in ${TARGETS_APPS}; do
		install -m 0755 ${S}/$app_name/files/$app_name ${D}${bindir}
	done
	install -d ${D}${sysconfdir}
	install -m 0644 ${S}/sdfec-demo/files/sdfec.conf ${D}${sysconfdir}/sdfec.conf
}
