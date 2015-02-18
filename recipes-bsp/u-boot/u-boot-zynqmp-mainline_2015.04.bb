require recipes-bsp/u-boot/u-boot.inc
require u-boot-elf.inc

DEPENDS += "dtc-native"

# This revision corresponds to the tag "v2015.04-rc1"
SRCREV = "112db9407dd338f71200beb0fc99dffa8dcb57a8"

PV = "v2015.04-rc1+git${SRCPV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-zynqmp-mainline:"
SRC_URI_append += " \
		file://0001-arm64-Add-Xilinx-ZynqMP-support.patch \
		"

