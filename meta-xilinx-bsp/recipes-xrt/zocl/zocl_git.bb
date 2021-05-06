SUMMARY  = "Xilinx Runtime(XRT) driver module"
DESCRIPTION = "Xilinx Runtime driver module provides memory management and compute unit schedule"


require recipes-xrt/xrt/xrt.inc

LIC_FILES_CHKSUM = "file://LICENSE;md5=7d040f51aae6ac6208de74e88a3795f8"
LICENSE = "GPLv2 & Apache-2.0"

S = "${WORKDIR}/git/src/runtime_src/core/edge/drm/zocl"

inherit module

pkg_postinst_ontarget_${PN}() {
  #!/bin/sh
  echo "Unloading old XRT Linux kernel modules"
  ( rmmod zocl || true ) > /dev/null 2>&1
  echo "Loading new XRT Linux kernel modules"
  modprobe zocl
}
