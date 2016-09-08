SUMMARY = "A Mali 400 Linux Kernel module"
SECTION = "kernel/modules"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = " \
	file://linux/license/gpl/mali_kernel_license.h;md5=68c66513a9dacef77a52c3d6c5e6afd5 \
	"

PV = "r5p1-01rel0"

SRC_URI = " \
	http://malideveloper.arm.com/downloads/drivers/DX910/${PV}/DX910-SW-99002-${PV}.tgz \
	file://Makefile.patch \
	file://0002-staging-mali-r5p1-01rel0-Add-the-ZYNQ-ZYNQMP-platfor.patch \
	file://0003-staging-mali-r5p1-01rel0-Remove-unused-trace-macros.patch \
	file://0004-staging-mali-r5p1-01rel0-Don-t-include-mali_read_phy.patch \
	"
SRC_URI[md5sum] = "9c85c113e4d41ae992e45ba27287d1ab"
SRC_URI[sha256sum] = "86209c99c36a7622402b016b6f764c212b738ccdec9cdc6d6f16758c013957a0"

inherit module

do_make_scripts[depends] += "virtual/kernel:do_unpack"

S = "${WORKDIR}/driver/src/devicedrv/mali"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmp = "zynqmp"

EXTRA_OEMAKE = 'KDIR="${STAGING_KERNEL_DIR}" \
		ARCH="${ARCH}" \
		BUILD=release \
		MALI_PLATFORM="arm" \
		USING_DT=1 \
		MALI_SHARED_INTERRUPTS=1 \
		CROSS_COMPILE="${TARGET_PREFIX}" \
		O=${STAGING_KERNEL_BUILDDIR} \
		'
