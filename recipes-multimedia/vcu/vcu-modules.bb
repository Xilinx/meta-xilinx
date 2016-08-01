SUMMARY = "Video Codec Unit (VCU) Linux Kernel module"
SECTION = "kernel/modules"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = " \
	file://LICENSE.md;md5=eb723b61539feef013de476e68b5c50a \
	"

PV = "alpha0"

SRC_URI = " \
	git://github.com/Xilinx/vcu-modules.git;protocol=https;nobranch=1 \
	"
SRC_URI[md5sum] = "9c85c113e4d41ae992e45ba27287d1ab"
SRC_URI[sha256sum] = "86209c99c36a7622402b016b6f764c212b738ccdec9cdc6d6f16758c013957a0"
SRCREV = "2016.4_alpha0"

inherit module

do_make_scripts[depends] += "virtual/kernel:do_configure"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE_zynqmp = "(.*)"

EXTRA_OEMAKE = 'KERNEL_SRC="${STAGING_KERNEL_DIR}" \
 		O=${STAGING_KERNEL_BUILDDIR} \
 		'
