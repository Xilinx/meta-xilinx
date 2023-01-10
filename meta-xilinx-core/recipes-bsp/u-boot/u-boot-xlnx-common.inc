# Use the name of the .bb for the extension
UBOOT_VERSION_EXTENSION ?= "-xilinx-v${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or ''}"
PV = "${UBOOT_VERSION}${UBOOT_VERSION_EXTENSION}+git${SRCPV}"

UBOOTURI ?= "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https"
UBRANCH ?= ""
UBRANCHARG = "${@['nobranch=1', 'branch=${UBRANCH}'][d.getVar('UBRANCH', True) != '']}"

SRC_URI = "${UBOOTURI};${UBRANCHARG}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

FILESEXTRAPATHS:prepend := "${THISDIR}/u-boot-${UBOOT_VERSION}:"

UBOOT_MB_INC = ""
UBOOT_MB_INC:microblaze = "u-boot-xlnx-microblaze.inc"
require ${UBOOT_MB_INC}

