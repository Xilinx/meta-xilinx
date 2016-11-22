DESCRIPTION = "ARM Trusted Firmware"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.md;md5=829bdeb34c1d9044f393d5a16c068371"

inherit deploy

DEPENDS += "u-boot-mkimage-native"

S = "${WORKDIR}/git"

BRANCH = "master"
SRC_URI = "git://github.com/Xilinx/arm-trusted-firmware.git;protocol=https;branch=${BRANCH}"

# This points at the 'xilinx-v2016.2' tag
SRCREV ?= "1058495c23e3c759c1c3785432b4d8eaa4fc7340"

PV = "1.2+xilinx+git${SRCPV}"

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

OUTPUT_DIR = "${S}/build/${PLATFORM}/release"

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0644 ${OUTPUT_DIR}/bl31/bl31.elf ${DEPLOYDIR}/bl31-${MACHINE}.elf
	install -m 0644 ${OUTPUT_DIR}/bl31.bin ${DEPLOYDIR}/bl31-${MACHINE}.bin

	# Get the entry point address from the elf.
	BL31_BASE_ADDR=$(${READELF} -h ${OUTPUT_DIR}/bl31/bl31.elf | egrep -m 1 -i "entry point.*?0x" | sed -r 's/.*?(0x.*?)/\1/g')
	mkimage -A arm64 -O linux -T kernel -C none \
		-a $BL31_BASE_ADDR -e $BL31_BASE_ADDR \
		-d ${OUTPUT_DIR}/bl31.bin ${DEPLOYDIR}/atf.ub
}
addtask deploy before do_build after do_compile
