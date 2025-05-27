UBRANCH = "xlnx_rebase_v2025.01"

SRCREV = "a09d2660433d3d1ade6a14a82b21a13c2bbb3821"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://README;beginline=1;endline=4;md5=744e7e3bb0c94b4b9f6b3db3bf893897"

# u-boot-xlnx has support for these
HAS_PLATFORM_INIT ?= " \
		xilinx_zynqmp_virt_config \
		xilinx_zynq_virt_defconfig \
		xilinx_versal_vc_p_a2197_revA_x_prc_01_revA \
		"

DEPENDS += "bc-native dtc-native python3-setuptools-native gnutls-native"

FILESEXTRAPATHS:prepend := "${THISDIR}/u-boot-v2025.01:"

SRC_URI:append:versal-2ve-2vm = "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'file://u-boot-optee.cfg', '', d)} \
    file://0001-amd_versal_2ve_2vm-Add-ufs-distro-boot-command.patch \
    file://0001-Reread-the-eeprom-in-the-late_board_init.patch \
    "
