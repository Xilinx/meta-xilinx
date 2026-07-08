require recipes-bsp/trusted-firmware-a/trusted-firmware-a.inc
require trusted-firmware-a-xlnx.inc

DEPENDS:remove:zynqmp:qemuall = " optee-os"
DEPENDS:remove:versal:qemuall = " optee-os"
DEPENDS:remove:versal-net:qemuall = " optee-os"

# Xilinx TF-A v2.12
SRC_URI_TRUSTED_FIRMWARE_A = "git://github.com/Xilinx/arm-trusted-firmware.git;protocol=https"
SRCREV_tfa = "12f045bd3a81d6fd99dc7470c12a459ae797888f"
SRCBRANCH = "xlnx_rebase_v2.12"

LIC_FILES_CHKSUM = "file://docs/license.rst;md5=83b7626b8c7a37263c6a58af8d19bee1"

# mbedtls-3.4.1 is not enabled in this configuration

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
EXTRA_OEMAKE:append = " LD='${@remove_options_tail(d.getVar('CC'))}'"

# We use bl31
TFA_BUILD_TARGET = "bl31"
TFA_INSTALL_TARGET = "bl31"

inherit image-artifact-names

inherit deploy

DEPENDS += "u-boot-mkimage-native"

do_deploy() {
    install -d -m 755 ${DEPLOYDIR}

    for atfbin in ${TFA_INSTALL_TARGET}; do
        processed="0"
        if [ "$atfbin" = "all" ]; then
            # Target all is not handled by default
            bberror "all as TFA_INSTALL_TARGET is not handled by do_install"
            bberror "Please specify valid targets in TFA_INSTALL_TARGET or"
            bberror "rewrite or turn off do_install"
            exit 1
        fi

        if [ -f ${BUILD_DIR}/$atfbin.bin ]; then
            echo "Install $atfbin.bin"
            install -m 0644 ${BUILD_DIR}/$atfbin.bin \
                ${DEPLOYDIR}/$atfbin${TFA_INSTALL_SUFFIX}.bin
            processed="1"
        fi
        if [ -f ${BUILD_DIR}/$atfbin/$atfbin.elf ]; then
            echo "Install $atfbin.elf"
            install -m 0644 ${BUILD_DIR}/$atfbin/$atfbin.elf \
                ${DEPLOYDIR}/$atfbin${TFA_INSTALL_SUFFIX}.elf
            processed="1"
        fi
        if [ -f ${BUILD_DIR}/$atfbin ]; then
            echo "Install $atfbin"
            install -m 0644 ${BUILD_DIR}/$atfbin \
                ${DEPLOYDIR}/$atfbin${TFA_INSTALL_SUFFIX}
            processed="1"
        fi
        if [ "$processed" = "0" ]; then
            bberror "Unsupported TFA_INSTALL_TARGET target $atfbin"
            exit 1
        fi
    done
}

addtask deploy before do_build after do_compile

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
