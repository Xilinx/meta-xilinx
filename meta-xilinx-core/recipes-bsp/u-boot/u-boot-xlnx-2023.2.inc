UBRANCH = "xlnx_rebase_v2023.01"

SRCREV = "2b54ad6f1d278c419b927d10db2edf323d844578"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://README;beginline=1;endline=4;md5=744e7e3bb0c94b4b9f6b3db3bf893897"

# u-boot-xlnx has support for these
HAS_PLATFORM_INIT ?= " \
		xilinx_zynqmp_virt_config \
		xilinx_zynq_virt_defconfig \
		xilinx_versal_vc_p_a2197_revA_x_prc_01_revA \
		"

DEPENDS += "bc-native dtc-native python3-setuptools-native gnutls-native"

SRC_URI:append:microblaze = " file://microblaze-generic-top.h"

FILESEXTRAPATHS:prepend := "${THISDIR}/u-boot-v2023.01:"

do_configure:prepend:microblaze () {
    install ${WORKDIR}/microblaze-generic-top.h ${S}/include/configs/
}
