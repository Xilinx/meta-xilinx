SUMMARY = "Firmware for VCU2"
DESCRIPTION = "Firmware binaries provider for VCU2"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=323caf712b1e8006fbec07506fe71db0"

PV .= "+git"

S  = "${WORKDIR}/git"

BRANCH ?="xlnx_rel_v2024.1"
REPO ?= "git://github.com/Xilinx/vcu2-firmware.git;protocol=https"
SRCREV = "79e8d90a22eb38de9dc9725bffda57a29bd75f10"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI   = "${REPO};${BRANCHARG}"

inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_compile[noexec] = "1"

do_install() {
    install -Dm 0644 ${S}/decoder_firmware.bin ${D}${nonarch_base_libdir}/firmware/ald3xx.fw
    install -Dm 0644 ${S}/encoder_firmware.bin ${D}${nonarch_base_libdir}/firmware/ale2xx.fw
}

# Inhibit warnings about files being stripped
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
FILES:${PN} = "${nonarch_base_libdir}/firmware/*"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.
EXCLUDE_FROM_WORLD = "1"

INSANE_SKIP:${PN} = "ldflags"
