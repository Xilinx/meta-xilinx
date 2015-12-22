SUMMARY = "External Xilinx toolchain"
include xilinx-toolchain.inc

PN .= "-${TARGET_ARCH}"
BPN = "binutils"

PV = "${XILINX_VER_MAIN}"
PKGV = "${XILINX_VER_GCC}"

PR =  "r1"

LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

PROVIDES = "virtual/${TARGET_PREFIX}binutils"

FILES_${PN}-dbg=""
FILES_${PN}-dev=""

ALLOW_EMPTY_${PN}-dev = "0"
ALLOW_EMPTY_${PN}-dbg = "0"

INHIBIT_AUTOTOOLS_DEPS = "1"
INHIBIT_DEFAULT_DEPS = "1"
