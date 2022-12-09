SUMMARY = "Firmware for VCU"
DESCRIPTION = "Firmware binaries provider for VCU"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE;md5=63b45903a9a50120df488435f03cf498"

XILINX_VCU_VERSION = "1.0.0"
PV = "${XILINX_VCU_VERSION}-xilinx-v${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or ''}+git${SRCPV}"

S  = "${WORKDIR}/git"

BRANCH ?= "xlnx_rel_v2022.1"
REPO ?= "git://github.com/Xilinx/vcu-firmware.git;protocol=https"
SRCREV = "569f980527fd58f43baf16bd0b294bf8c7cdf963"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI   = "${REPO};${BRANCHARG}"

inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -Dm 0644 ${S}/${XILINX_VCU_VERSION}/lib/firmware/al5d_b.fw ${D}/lib/firmware/al5d_b.fw
    install -Dm 0644 ${S}/${XILINX_VCU_VERSION}/lib/firmware/al5d.fw ${D}/lib/firmware/al5d.fw
    install -Dm 0644 ${S}/${XILINX_VCU_VERSION}/lib/firmware/al5e_b.fw ${D}/lib/firmware/al5e_b.fw
    install -Dm 0644 ${S}/${XILINX_VCU_VERSION}/lib/firmware/al5e.fw ${D}/lib/firmware/al5e.fw
}

# Inhibit warnings about files being stripped
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
FILES:${PN} = "/lib/firmware/*"

# These libraries shouldn't get installed in world builds unless something
# explicitly depends upon them.
EXCLUDE_FROM_WORLD = "1"

INSANE_SKIP:${PN} = "ldflags"
