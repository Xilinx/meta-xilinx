require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native"

UBOOTURI ?= "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https"
UBRANCH ?= "master"
UBRANCHARG = "${@['nobranch=1', 'branch=${UBRANCH}'][d.getVar('UBRANCH', True) != '']}"
SRC_URI = "${UBOOTURI};${UBRANCHARG}"

S = "${WORKDIR}/git"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot:"
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-xlnx:"
FILESEXTRAPATHS_prepend := "${@'${THISDIR}/u-boot-xlnx/${XILINX_RELEASE_VERSION}:' if d.getVar('XILINX_RELEASE_VERSION') else ''}"

do_deploy_append_zcu102-zynqmp () {
	# deploy u-boot-spl.bin for use by runqemu/QEMU
	install -Dm 0644 ${B}/spl/u-boot-spl.bin ${DEPLOYDIR}/u-boot-spl.bin
}
