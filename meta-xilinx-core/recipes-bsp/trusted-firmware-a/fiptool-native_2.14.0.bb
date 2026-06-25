DEFAULT_PREFERENCE = "-1"

# Firmware Image Package (FIP)
# It is a packaging format used by TF-A to package the
# firmware images in a single binary.

SUMMARY = "Native fiptool (TF-A v2.14.0) for packaging Firmware Image \
Packages (FIPs)."
DESCRIPTION = "fiptool - Trusted Firmware tool for packaging"
LICENSE = "BSD-3-Clause"

SRC_URI_TRUSTED_FIRMWARE_A ?= "git://git.trustedfirmware.org/TF-A/trusted-firmware-a.git;protocol=https"
SRC_URI = "${SRC_URI_TRUSTED_FIRMWARE_A};destsuffix=fiptool-${PV};branch=${SRCBRANCH}"
LIC_FILES_CHKSUM = "file://docs/license.rst;md5=6ed7bace7b0bc63021c6eba7b524039e"

# Use fiptool from TF-A v2.14.0
SRCREV = "1d5aa939bc8d3d892e2ed9945fa50e36a1a924cc"
SRCBRANCH = "master"

DEPENDS += "openssl-native"

inherit native

EXTRA_OEMAKE = "V=1 HOSTCC='${BUILD_CC}' OPENSSL_DIR=${STAGING_DIR_NATIVE}/${prefix_native}"

do_compile () {
    # This is still needed to have the native fiptool executing properly by
    # setting the RPATH
    sed -i '/^LDOPTS/ s,$, \$\{BUILD_LDFLAGS},' ${S}/tools/fiptool/Makefile
    sed -i '/^INCLUDE_PATHS/ s,$, \$\{BUILD_CFLAGS},' ${S}/tools/fiptool/Makefile

    oe_runmake fiptool
}

do_install () {
    install -D -p -m 0755 tools/fiptool/fiptool ${D}${bindir}/fiptool
}
