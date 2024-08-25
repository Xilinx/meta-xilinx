DESCRIPTION = "ARM Trusted Firmware"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM ?= "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

PROVIDES = "virtual/arm-trusted-firmware"

inherit deploy

DEPENDS += "u-boot-mkimage-native"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

SYSROOT_DIRS += "/boot"

PV .= "+git"

BRANCH ?= ""
REPO ?= "git://github.com/Xilinx/arm-trusted-firmware.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

inherit image-artifact-names

ATF_BASE_NAME ?= "${PN}-${PKGE}-${PKGV}-${PKGR}${IMAGE_VERSION_SUFFIX}"

COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PLATFORM:zynqmp = "zynqmp"
PLATFORM:versal = "versal"
PLATFORM:versal-net = "versal_net"

# requires CROSS_COMPILE set by hand as there is no configure script
export CROSS_COMPILE="${TARGET_PREFIX}"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

ATF_CONSOLE_DEFAULT = ""
ATF_CONSOLE_DEFAULT:zynqmp = "cadence"
ATF_CONSOLE_DEFAULT:versal = "pl011"
ATF_CONSOLE_DEFAULT:versal-net = "pl011"
ATF_CONSOLE ?= "${ATF_CONSOLE_DEFAULT}"

DEBUG_ATF_DEFAULT = ""
DEBUG_ATF_DEFAULT:versal = "1"
DEBUG_ATF_DEFAULT:versal-net = "1"
DEBUG_ATF ?= "${DEBUG_ATF_DEFAULT}"

EXTRA_OEMAKE:append:zynqmp = "${@' ZYNQMP_CONSOLE=${ATF_CONSOLE}' if d.getVar('ATF_CONSOLE', True) != '' else ''}"
EXTRA_OEMAKE:append:versal = "${@' VERSAL_CONSOLE=${ATF_CONSOLE}' if d.getVar('ATF_CONSOLE', True) != '' else ''}"
EXTRA_OEMAKE:append:versal-net = "${@' VERSAL_NET_CONSOLE=${ATF_CONSOLE}' if d.getVar('ATF_CONSOLE', True) != '' else ''}"
EXTRA_OEMAKE:append = " ${@bb.utils.contains('DEBUG_ATF', '1', ' DEBUG=${DEBUG_ATF}', '', d)}"

OUTPUT_DIR = "${@bb.utils.contains('DEBUG_ATF', '1', '${B}/${PLATFORM}/debug', '${B}/${PLATFORM}/release', d)}"

ATF_MEM_BASE ?= ""
ATF_MEM_SIZE ?= ""

EXTRA_OEMAKE:append:zynqmp = "${@' ZYNQMP_ATF_MEM_BASE=${ATF_MEM_BASE}' if d.getVar('ATF_MEM_BASE', True) != '' else ''}"
EXTRA_OEMAKE:append:zynqmp = "${@' ZYNQMP_ATF_MEM_SIZE=${ATF_MEM_SIZE}' if d.getVar('ATF_MEM_SIZE', True) != '' else ''}"

EXTRA_OEMAKE:append:versal = "${@' VERSAL_ATF_MEM_BASE=${ATF_MEM_BASE}' if d.getVar('ATF_MEM_BASE', True) != '' else ''}"
EXTRA_OEMAKE:append:versal = "${@' VERSAL_ATF_MEM_SIZE=${ATF_MEM_SIZE}' if d.getVar('ATF_MEM_SIZE', True) != '' else ''}"

EXTRA_OEMAKE:append:versal-net = "${@' VERSAL_NET_ATF_MEM_BASE=${ATF_MEM_BASE}' if d.getVar('ATF_MEM_BASE', True) != '' else ''}"
EXTRA_OEMAKE:append:versal-net = "${@' VERSAL_NET_ATF_MEM_SIZE=${ATF_MEM_SIZE}' if d.getVar('ATF_MEM_SIZE', True) != '' else ''}"

TFA_BL33_LOAD ?= ""
EXTRA_OEMAKE:append = "${@' PRELOADED_BL33_BASE=${TFA_BL33_LOAD}' if d.getVar('TFA_BL33_LOAD', True) != '' else ''}"

# Workaround for bl31.elf has a LOAD segment with RWX permissions
EXTRA_OEMAKE:append = ' TF_LDFLAGS="--no-warn-rwx-segments --fatal-warnings -O1 --gc-sections"'

do_configure() {
	oe_runmake clean -C ${S} BUILD_BASE=${B} PLAT=${PLATFORM}
}

do_compile() {
	oe_runmake -C ${S} BUILD_BASE=${B} PLAT=${PLATFORM} RESET_TO_BL31=1 bl31 V=1
}

do_install() {
	install -d ${D}/boot
	install -Dm 0644 ${OUTPUT_DIR}/bl31/bl31.elf ${D}/boot/${ATF_BASE_NAME}.elf
	ln -sf ${ATF_BASE_NAME}.elf ${D}/boot/${PN}.elf
	install -m 0644 ${OUTPUT_DIR}/bl31.bin ${D}/boot/${ATF_BASE_NAME}.bin
	ln -sf ${ATF_BASE_NAME}.bin ${D}/boot/${PN}.bin

	# Get the entry point address from the elf.
	BL31_BASE_ADDR=$(${READELF} -h ${OUTPUT_DIR}/bl31/bl31.elf | egrep -m 1 -i "entry point.*?0x" | sed -r 's/.*?(0x.*?)/\1/g')
	mkimage -A arm64 -O arm-trusted-firmware -T kernel -C none \
		-a $BL31_BASE_ADDR -e $BL31_BASE_ADDR \
		-d ${OUTPUT_DIR}/bl31.bin ${D}/boot/${ATF_BASE_NAME}.ub
	ln -sf ${ATF_BASE_NAME}.ub ${D}/boot/${PN}.ub
	ln -sf ${ATF_BASE_NAME}.ub ${D}/boot/atf-uboot.ub
}

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0644 ${OUTPUT_DIR}/bl31/bl31.elf ${DEPLOYDIR}/${ATF_BASE_NAME}.elf
	ln -sf ${ATF_BASE_NAME}.elf ${DEPLOYDIR}/${PN}.elf
	install -m 0644 ${OUTPUT_DIR}/bl31.bin ${DEPLOYDIR}/${ATF_BASE_NAME}.bin
	ln -sf ${ATF_BASE_NAME}.bin ${DEPLOYDIR}/${PN}.bin

	# Get the entry point address from the elf.
	BL31_BASE_ADDR=$(${READELF} -h ${OUTPUT_DIR}/bl31/bl31.elf | egrep -m 1 -i "entry point.*?0x" | sed -r 's/.*?(0x.*?)/\1/g')
	mkimage -A arm64 -O arm-trusted-firmware -T kernel -C none \
		-a $BL31_BASE_ADDR -e $BL31_BASE_ADDR \
		-d ${OUTPUT_DIR}/bl31.bin ${DEPLOYDIR}/${ATF_BASE_NAME}.ub
	ln -sf ${ATF_BASE_NAME}.ub ${DEPLOYDIR}/${PN}.ub
	ln -sf ${ATF_BASE_NAME}.ub ${DEPLOYDIR}/atf-uboot.ub
}
addtask deploy before do_build after do_compile
FILES:${PN} += "/boot/*.elf /boot/*.bin /boot/*.ub"

# Disable buildpaths QA check warnings for Versal.
INSANE_SKIP:${PN}:append:versal = " buildpaths"
INSANE_SKIP:${PN}:append:versal-net = " buildpaths"

python() {
    soc_family = d.getVar('SOC_FAMILY')
    atf_console = d.getVar('ATF_CONSOLE')

    # See plat/xilinx/<soc_family>/platform.mk 
    if soc_family and soc_family == "zynqmp":
        if not atf_console in [ 'cadence', 'cadence0', 'cadence1', 'dcc' ]:
            raise bb.parse.SkipRecipe('ATF_CONSOLE (%s) is not configured properly for ZynqMP, only cadence, cadence0, cadence1, and dcc are valid options.' % (atf_console))
    elif soc_family and soc_family == "versal":
        if not atf_console in [ 'pl011', 'pl011_0', 'pl011_1', 'dcc' ]:
            raise bb.parse.SkipRecipe('ATF_CONSOLE (%s) is not configured properly for Versal, only pl011, pl011_0, pl011_1, and dcc are valid options.' % (atf_console))
    elif soc_family and soc_family == "versal-net":
        if not atf_console in [ 'pl011', 'pl011_0', 'pl011_1', 'dcc' ]:
            raise bb.parse.SkipRecipe('ATF_CONSOLE (%s) is not configured properly for Versal-Net, only pl011, pl011_0, pl011_1, and dcc are valid options.' % (atf_console))
}
