FILESEXTRAPATHS:prepend:zynqmp := "${THISDIR}/files/9.0.0:${THISDIR}/files:"

require weston.inc

# ARGB fallback is only needed (and only applies) on the weston 9 sources used
# with the closed libmali driver on ZynqMP; weston 15 neither needs nor accepts
# this patch.
#
# This is a separate MACHINE_ARCH trigger from the driver-based one in
# weston.inc. weston.inc already makes weston MACHINE_ARCH on ANY board whenever
# it links the closed libmali blob (that applies to weston 15 too). Here we add
# a second, zynqmp-only reason: applying a machine-specific source patch means
# the resulting package is no longer generic, so it must be MACHINE_ARCH for the
# machines that get the patch. Since the patch is applied :zynqmp, the arch
# override is :zynqmp as well.
SRC_URI:append:zynqmp = " file://0001-Force-weston-to-select-ARGB-if-XRGB-is-not-available.patch"
PACKAGE_ARCH:zynqmp = "${MACHINE_ARCH}"
