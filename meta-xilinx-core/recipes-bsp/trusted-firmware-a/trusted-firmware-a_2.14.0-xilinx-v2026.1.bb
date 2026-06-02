require recipes-bsp/trusted-firmware-a/trusted-firmware-a.inc
require trusted-firmware-a-xlnx.inc

DEPENDS:remove:zynqmp:qemuall = " optee-os"
DEPENDS:remove:versal:qemuall = " optee-os"
DEPENDS:remove:versal-net:qemuall = " optee-os"

# Xilinx TF-A v2.14
SRC_URI_TRUSTED_FIRMWARE_A = "git://github.com/Xilinx/arm-trusted-firmware.git;protocol=https"
SRCREV_tfa = "e9b1373a7dd9fce25b5a2fe6189dd5ec6eedf24a"
SRCBRANCH = "xlnx_rebase_v2.14"

LIC_FILES_CHKSUM = "file://docs/license.rst;md5=6ed7bace7b0bc63021c6eba7b524039e"

# mbedtls-3.4.1 is not enabled in this configuration

# SCMI Server support (set TFA_SCMI_SERVER to 1 to activate)
TFA_SCMI_SERVER ?= "0"

# SCMI Server repository details
SRCBRANCH_scmi-server = "main"
SRC_URI_SCMI_SERVER ?= "git://github.com/Xilinx/scmi-server.git;name=scmi-server;protocol=https;destsuffix=scmi-server;branch=${SRCBRANCH_scmi-server}"
SRCREV_scmi-server = "cdcb7dc09ceb8718929eb7a712dc793018aa8f43"

# Conditionally add SCMI server to sources
SRC_URI:append = " ${@bb.utils.contains('TFA_SCMI_SERVER', '1', '${SRC_URI_SCMI_SERVER}', '', d)}"

# Update license information (adjust license type as needed)
LICENSE:append = "${@bb.utils.contains('TFA_SCMI_SERVER', '1', ' & BSD-3-Clause', '', d)}"
LIC_FILES_CHKSUM += "${@bb.utils.contains('TFA_SCMI_SERVER', '1', 'file://../scmi-server/LICENSE;md5=8a98de6f98ae5501d035c335a47a2bae', '', d)}"

SRCREV = "e9b1373a7dd9fce25b5a2fe6189dd5ec6eedf24a"
SRCREV_FORMAT:append = "${@bb.utils.contains('TFA_SCMI_SERVER', '1', '_scmi-server', '', d)}"

# Pass SCMI server path to TF-A build
EXTRA_OEMAKE += "${@bb.utils.contains('TFA_SCMI_SERVER', '1', 'CUSTOM_PKG_PATH=${UNPACKDIR}/scmi-server', '', d)}"


COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

### Platform Settings
TFA_PLATFORM:zynqmp     = "zynqmp"
TFA_PLATFORM:versal     = "versal"
TFA_PLATFORM:versal-net = "versal_net"
TFA_PLATFORM:versal-2ve-2vm = "versal2"

### Console settings
TFA_CONSOLE_DEFAULT = ""
TFA_CONSOLE_DEFAULT:zynqmp = "cadence"
TFA_CONSOLE_DEFAULT:versal = "pl011"
TFA_CONSOLE_DEFAULT:versal-net = "pl011"
TFA_CONSOLE_DEFAULT:versal-2ve-2vm = "pl011"

TFA_CONSOLE ?= "${TFA_CONSOLE_DEFAULT}"

TFA_CONSOLE_OEMAKE = ""
TFA_CONSOLE_OEMAKE:append:zynqmp = "${@' ZYNQMP_CONSOLE=${TFA_CONSOLE}' if d.getVar('TFA_CONSOLE') != '' else ''}"
TFA_CONSOLE_OEMAKE:append:versal = "${@' VERSAL_CONSOLE=${TFA_CONSOLE}' if d.getVar('TFA_CONSOLE') != '' else ''}"
TFA_CONSOLE_OEMAKE:append:versal-net = "${@' VERSAL_NET_CONSOLE=${TFA_CONSOLE}' if d.getVar('TFA_CONSOLE') != '' else ''}"
TFA_CONSOLE_OEMAKE:append:versal-2ve-2vm = "${@' CONSOLE=${TFA_CONSOLE}' if d.getVar('TFA_CONSOLE') != '' else ''}"

EXTRA_OEMAKE += "${TFA_CONSOLE_OEMAKE}"

### Debug settings
TFA_DEBUG ?= "0"


### Mem Settings
TFA_MEM_BASE ?= ""
TFA_MEM_SIZE ?= ""

TFA_MEM_OEMAKE = ""
TFA_MEM_OEMAKE:append:zynqmp     = "${@' ZYNQMP_ATF_MEM_BASE=${TFA_MEM_BASE}'     if d.getVar('TFA_MEM_BASE') != '' else ''}"
TFA_MEM_OEMAKE:append:zynqmp     = "${@' ZYNQMP_ATF_MEM_SIZE=${TFA_MEM_SIZE}'     if d.getVar('TFA_MEM_SIZE') != '' else ''}"
TFA_MEM_OEMAKE:append:versal     = "${@' VERSAL_ATF_MEM_BASE=${TFA_MEM_BASE}'     if d.getVar('TFA_MEM_BASE') != '' else ''}"
TFA_MEM_OEMAKE:append:versal     = "${@' VERSAL_ATF_MEM_SIZE=${TFA_MEM_SIZE}'     if d.getVar('TFA_MEM_SIZE') != '' else ''}"
TFA_MEM_OEMAKE:append:versal-net = "${@' VERSAL_NET_ATF_MEM_BASE=${TFA_MEM_BASE}' if d.getVar('TFA_MEM_BASE') != '' else ''}"
TFA_MEM_OEMAKE:append:versal-net = "${@' VERSAL_NET_ATF_MEM_SIZE=${TFA_MEM_SIZE}' if d.getVar('TFA_MEM_SIZE') != '' else ''}"
TFA_MEM_OEMAKE:append:versal-2ve-2vm    = "${@' MEM_BASE=${TFA_MEM_BASE}' if d.getVar('TFA_MEM_BASE') != '' else ''}"
TFA_MEM_OEMAKE:append:versal-2ve-2vm    = "${@' MEM_SIZE=${TFA_MEM_SIZE}' if d.getVar('TFA_MEM_SIZE') != '' else ''}"

EXTRA_OEMAKE += "${TFA_MEM_OEMAKE}"


### Preloaded Base
TFA_BL33_LOAD ?= ""
EXTRA_OEMAKE += "${@' PRELOADED_BL33_BASE=${TFA_BL33_LOAD}' if d.getVar('TFA_BL33_LOAD') != '' else ''}"

# Enable PM for versal_2ve_2vm
EXTRA_OEMAKE:append:versal-2ve-2vm = " RESET_TO_BL31=1"

# Use opteed SPD (Secure Payload Dispatcher) for versal_2ve_2vm platform if optee
# MACHINE FEATURES is enabled.
TFA_SPD:versal-2ve-2vm ?= "${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'opteed', '', d)}"

# TFA 2.12+ with ENABLE_LTO=1 (zynqmp) requires gcc as linker driver for -fuse-linker-plugin
# The LD = "${CC}" approach does not reliably propagate via d.getVar('LD') in meta-arm.inc's
# Python expression; directly override LD in EXTRA_OEMAKE instead. Because GNU make uses the
# last command-line assignment for a variable, this appended LD= overrides the earlier one.
EXTRA_OEMAKE:append = " LD='${TARGET_PREFIX}gcc'"


# We use bl31
TFA_BUILD_TARGET = "bl31"
TFA_INSTALL_TARGET = "bl31"

inherit image-artifact-names

TFA_BASE_NAME ?= "${PN}-${PKGE}-${PKGV}-${PKGR}${IMAGE_VERSION_SUFFIX}"

do_install:append() {
    # The first TFA_INSTALL_TARGET found will be copied as the standard boot firmware
    # Uses ${FIRMWARE_DIR} (from firmware.bbclass: /firmware/${PN}) to match
    # meta-arm's install layout since commit 7bce36a2 (switched to firmware.bbclass).
    for tfabin in ${TFA_INSTALL_TARGET} ; do
        install -d ${D}/boot
        if [ -e ${D}${FIRMWARE_DIR}/$tfabin${TFA_INSTALL_SUFFIX}.elf ]; then
            ln ${D}${FIRMWARE_DIR}/$tfabin${TFA_INSTALL_SUFFIX}.elf ${D}/boot/${TFA_BASE_NAME}.elf
            ln -sf ${TFA_BASE_NAME}.elf ${D}/boot/${PN}.elf
            ln ${D}${FIRMWARE_DIR}/$tfabin${TFA_INSTALL_SUFFIX}.bin ${D}/boot/${TFA_BASE_NAME}.bin
            ln -sf ${TFA_BASE_NAME}.bin ${D}/boot/${PN}.bin

            # Get the entry point address from the elf.
            BL31_BASE_ADDR=$(${READELF} -h ${D}/boot/${TFA_BASE_NAME}.elf | egrep -m 1 -i "entry point.*?0x" | sed -r 's/.*?(0x.*?)/\1/g')
            mkimage -A arm64 -O trusted-firmware-a -T kernel -C none \
                    -a $BL31_BASE_ADDR -e $BL31_BASE_ADDR \
                    -d ${D}${FIRMWARE_DIR}/$tfabin${TFA_INSTALL_SUFFIX}.bin ${D}/boot/${TFA_BASE_NAME}.ub
            ln -sf ${TFA_BASE_NAME}.ub ${D}/boot/arm-trusted-firmware.ub
            ln -sf ${TFA_BASE_NAME}.ub ${D}/boot/tfa-uboot.ub
            break
        fi
    done
}

inherit deploy

DEPENDS += "u-boot-mkimage-native"

do_deploy() {
    # Copy the /boot items to deploy
    install -d ${DEPLOYDIR}
    cp -rf ${D}/boot/* ${DEPLOYDIR}/
}

addtask deploy before do_build after do_compile

SYSROOT_DIRS += "/boot"
FILES:${PN} += "/boot/*.elf /boot/*.bin /boot/*.ub"

python() {
    soc_family = d.getVar('SOC_FAMILY')
    tfa_console = d.getVar('TFA_CONSOLE')

    # See plat/xilinx/<soc_family>/platform.mk
    if soc_family and soc_family == "zynqmp":
        if not tfa_console in [ 'cadence', 'cadence0', 'cadence1', 'dcc' ]:
            raise bb.parse.SkipRecipe('TFA_CONSOLE (%s) is not configured properly for ZynqMP, only cadence, cadence0, cadence1, and dcc are valid options.' % (tfa_console))
    elif soc_family and soc_family == "versal":
        if not tfa_console in [ 'pl011', 'pl011_0', 'pl011_1', 'dcc' ]:
            raise bb.parse.SkipRecipe('TFA_CONSOLE (%s) is not configured properly for Versal, only pl011, pl011_0, pl011_1, and dcc are valid options.' % (tfa_console))
    elif soc_family and soc_family == "versal-net":
        if not tfa_console in [ 'pl011', 'pl011_0', 'pl011_1', 'dcc' ]:
            raise bb.parse.SkipRecipe('TFA_CONSOLE (%s) is not configured properly for Versal-Net, only pl011, pl011_0, pl011_1, and dcc are valid options.' % (tfa_console))
    elif soc_family and soc_family == "versal-2ve-2vm":
        if not tfa_console in [ 'pl011', 'pl011_0', 'pl011_1', 'dcc' ]:
            raise bb.parse.SkipRecipe('TFA_CONSOLE (%s) is not configured properly for Versal-2ve-2vm, only pl011, pl011_0, pl011_1, and dcc are valid options.' % (tfa_console))
}
