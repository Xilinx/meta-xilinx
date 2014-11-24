# Kernel version and SRCREV correspond to: xlnx_3.14 branch
LINUX_VERSION = "3.14"
KBRANCH ?= "xlnx_3.14"
SRCREV ?= "2b48a8aeea7367359f9eebe55c4a09a05227f32b"

include linux-xlnx.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx/3.14:"
SRC_URI_append += " \
		file://usb-host-zynq-dr-of-PHY-reset-during-probe.patch \
		"
