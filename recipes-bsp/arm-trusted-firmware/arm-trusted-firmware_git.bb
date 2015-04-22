DESCRIPTION = "ARM Trusted Firmware"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.md;md5=829bdeb34c1d9044f393d5a16c068371"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

S = "${WORKDIR}/git"

BRANCH = "master"
SRC_URI = "git://github.com/Xilinx/arm-trusted-firmware.git;protocol=https;branch=${BRANCH}"
SRCREV = "1d58628d0684522df7f87464850163ab6551afe9"

PV = "1.1+xilinx+git${SRCPV}"

COMPATIBLE_MACHINE = "zynqmp"
PLATFORM_zynqmp = "zynqmp"

# requires CROSS_COMPILE set by hand as there is no configure script
export CROSS_COMPILE="${TARGET_PREFIX}"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

do_configure() {
	:
}

do_compile() {
	oe_runmake PLAT=${PLATFORM} RESET_TO_BL31=1 bl31
}

do_install() {
	:
}

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0644 ${S}/build/${PLATFORM}/release/bl31/bl31.elf ${DEPLOYDIR}/bl31-${MACHINE}.elf
	install -m 0644 ${S}/build/${PLATFORM}/release/bl31.bin ${DEPLOYDIR}/bl31-${MACHINE}.bin
}
addtask deploy before do_build after do_compile
