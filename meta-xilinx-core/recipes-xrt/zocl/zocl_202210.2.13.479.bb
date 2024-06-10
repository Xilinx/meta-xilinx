SUMMARY  = "Xilinx Runtime(XRT) driver module"
DESCRIPTION = "Xilinx Runtime driver module provides memory management and compute unit schedule"


require recipes-xrt/xrt/xrt-${PV}.inc

LIC_FILES_CHKSUM = "file://LICENSE;md5=7d040f51aae6ac6208de74e88a3795f8"
LICENSE = "GPLv2 & Apache-2.0"

# Temporary fix
# Patch is applied as -p 4 to the src/runtime_src/core/edge directory
SRC_URI += "file://0001-Fixed-ZOCL-dtbo-path-len-issue-6966.patch;striplevel=5;patchdir=./../../"

S = "${WORKDIR}/git/src/runtime_src/core/edge/drm/zocl"

inherit module

pkg_postinst_ontarget:${PN}() {
  #!/bin/sh
  echo "Unloading old XRT Linux kernel modules"
  ( rmmod zocl || true ) > /dev/null 2>&1
  echo "Loading new XRT Linux kernel modules"
  modprobe zocl
}
