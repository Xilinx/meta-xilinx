require recipes-bsp/trusted-firmware-a/trusted-firmware-a.inc

DEPENDS:remove:zynqmp:qemuall = " optee-os"
DEPENDS:remove:versal:qemuall = " optee-os"
DEPENDS:remove:versal-net:qemuall = " optee-os"

# Xilinx TF-A v2.12
SRC_URI_TRUSTED_FIRMWARE_A = "git://github.com/Xilinx/arm-trusted-firmware.git;protocol=https"
SRCREV_tfa = "dd3512287a17eea866d5c82fa7b86ad58de59163"
SRCBRANCH = "xlnx_rebase_v2.12"

LIC_FILES_CHKSUM = "file://docs/license.rst;md5=83b7626b8c7a37263c6a58af8d19bee1"

# mbedtls-3.4.1 is not enabled in this configuration

# The following are Xilinx specific settings
PROVIDES = "virtual/arm-trusted-firmware"

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

# Use old name for compatibility
ATF_CONSOLE ?= "${TFA_CONSOLE_DEFAULT}"

# Old name to new name
TFA_CONSOLE ?= "${ATF_CONSOLE}"

TFA_CONSOLE_OEMAKE = ""
TFA_CONSOLE_OEMAKE:append:zynqmp = "${@' ZYNQMP_CONSOLE=${TFA_CONSOLE}' if d.getVar('TFA_CONSOLE', True) != '' else ''}"
TFA_CONSOLE_OEMAKE:append:versal = "${@' VERSAL_CONSOLE=${TFA_CONSOLE}' if d.getVar('TFA_CONSOLE', True) != '' else ''}"
TFA_CONSOLE_OEMAKE:append:versal-net = "${@' VERSAL_NET_CONSOLE=${TFA_CONSOLE}' if d.getVar('TFA_CONSOLE', True) != '' else ''}"
TFA_CONSOLE_OEMAKE:append:versal-2ve-2vm = "${@' CONSOLE=${TFA_CONSOLE}' if d.getVar('TFA_CONSOLE', True) != '' else ''}"

EXTRA_OEMAKE += "${TFA_CONSOLE_OEMAKE}"

### Debug settings
DEBUG_ATF_DEFAULT = ""
DEBUG_ATF_DEFAULT:versal = "1"
DEBUG_ATF_DEFAULT:versal-net = "1"
DEBUG_ATF_DEFAULT:versal-2ve-2vm = "1"
DEBUG_ATF ?= "${DEBUG_ATF_DEFAULT}"

# Translate old to new name
TFA_DEBUG = "${DEBUG_ATF}"


### Mem Settings
ATF_MEM_BASE ?= ""
ATF_MEM_SIZE ?= ""

TFA_MEM_BASE ?= "${ATF_MEM_BASE}"
TFA_MEM_SIZE ?= "${ATF_MEM_SIZE}"

TFA_MEM_OEMAKE = ""
TFA_MEM_OEMAKE:append:zynqmp     = "${@' ZYNQMP_ATF_MEM_BASE=${ATF_MEM_BASE}'     if d.getVar('ATF_MEM_BASE', True) != '' else ''}"
TFA_MEM_OEMAKE:append:zynqmp     = "${@' ZYNQMP_ATF_MEM_SIZE=${ATF_MEM_SIZE}'     if d.getVar('ATF_MEM_SIZE', True) != '' else ''}"
TFA_MEM_OEMAKE:append:versal     = "${@' VERSAL_ATF_MEM_BASE=${ATF_MEM_BASE}'     if d.getVar('ATF_MEM_BASE', True) != '' else ''}"
TFA_MEM_OEMAKE:append:versal     = "${@' VERSAL_ATF_MEM_SIZE=${ATF_MEM_SIZE}'     if d.getVar('ATF_MEM_SIZE', True) != '' else ''}"
TFA_MEM_OEMAKE:append:versal-net = "${@' VERSAL_NET_ATF_MEM_BASE=${ATF_MEM_BASE}' if d.getVar('ATF_MEM_BASE', True) != '' else ''}"
TFA_MEM_OEMAKE:append:versal-net = "${@' VERSAL_NET_ATF_MEM_SIZE=${ATF_MEM_SIZE}' if d.getVar('ATF_MEM_SIZE', True) != '' else ''}"
TFA_MEM_OEMAKE:append:versal-2ve-2vm    = "${@' MEM_BASE=${ATF_MEM_BASE}' if d.getVar('ATF_MEM_BASE', True) != '' else ''}"
TFA_MEM_OEMAKE:append:versal-2ve-2vm    = "${@' MEM_SIZE=${ATF_MEM_SIZE}' if d.getVar('ATF_MEM_SIZE', True) != '' else ''}"

EXTRA_OEMAKE += "${TFA_MEM_OEMAKE}"


### Preloaded Base
TFA_BL33_LOAD ?= ""
EXTRA_OEMAKE += "${@' PRELOADED_BL33_BASE=${TFA_BL33_LOAD}' if d.getVar('TFA_BL33_LOAD', True) != '' else ''}"

# Enable PM for versal_2ve_2vm
EXTRA_OEMAKE:append:versal-2ve-2vm = " RESET_TO_BL31=1"

# Use opteed SPD (Secure Payload Dispatcher) for versal_2ve_2vm platform if optee
# MACHINE FEATURES is enabled.
TFA_SPD:versal-2ve-2vm ?= "${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'opteed', '', d)}"

# TFA 2.12 seems to want to use gcc for linking instead of ld
LD = "${CCLD}"


# We use bl31
TFA_BUILD_TARGET = "bl31"
TFA_INSTALL_TARGET = "bl31"

inherit image-artifact-names

# arm-trusted-firmware instead of ${PN} for compatibility
ATF_BASE_NAME ?= "arm-trusted-firmware-${PKGE}-${PKGV}-${PKGR}${IMAGE_VERSION_SUFFIX}"

do_install:append() {
    # The first TFA_INSTALL_TARGET found will be copied as the standard boot firmware
    for atfbin in ${TFA_INSTALL_TARGET} ; do
        install -d ${D}/boot
        if [ -e ${D}/firmware/$atfbin-${TFA_PLATFORM}.elf ]; then
            ln ${D}/firmware/$atfbin-${TFA_PLATFORM}.elf ${D}/boot/${ATF_BASE_NAME}.elf
            ln -sf ${ATF_BASE_NAME}.elf ${D}/boot/arm-trusted-firmware.elf
            ln ${D}/firmware/$atfbin-${TFA_PLATFORM}.bin ${D}/boot/${ATF_BASE_NAME}.bin
            ln -sf ${ATF_BASE_NAME}.bin ${D}/boot/arm-trusted-firmware.bin

            # Get the entry point address from the elf.
            BL31_BASE_ADDR=$(${READELF} -h ${D}/boot/${ATF_BASE_NAME}.elf | egrep -m 1 -i "entry point.*?0x" | sed -r 's/.*?(0x.*?)/\1/g')
            mkimage -A arm64 -O arm-trusted-firmware -T kernel -C none \
                    -a $BL31_BASE_ADDR -e $BL31_BASE_ADDR \
                    -d ${D}/firmware/$atfbin-${TFA_PLATFORM}.bin ${D}/boot/${ATF_BASE_NAME}.ub
            ln -sf ${ATF_BASE_NAME}.ub ${D}/boot/arm-trusted-firmware.ub
            ln -sf ${ATF_BASE_NAME}.ub ${D}/boot/atf-uboot.ub
            break
        fi
    done
}

inherit deploy

DEPENDS += "u-boot-mkimage-native"

do_deploy() {
    # Copy the /boot items to deploy
    install -d ${DEPLOYDIR}
    install -m 0644 ${D}/boot/${ATF_BASE_NAME}.elf ${DEPLOYDIR}/${ATF_BASE_NAME}.elf
    ln -sf ${ATF_BASE_NAME}.elf ${DEPLOYDIR}/arm-trusted-firmware.elf
    install -m 0644 ${D}/boot/${ATF_BASE_NAME}.bin ${DEPLOYDIR}/${ATF_BASE_NAME}.bin
    ln -sf ${ATF_BASE_NAME}.bin ${DEPLOYDIR}/arm-trusted-firmware.bin

    install -m 0644 ${D}/boot/${ATF_BASE_NAME}.ub ${DEPLOYDIR}/${ATF_BASE_NAME}.ub
    ln -sf ${ATF_BASE_NAME}.ub ${DEPLOYDIR}/arm-trusted-firmware.ub
    ln -sf ${ATF_BASE_NAME}.ub ${DEPLOYDIR}/atf-uboot.ub
}

addtask deploy before do_build after do_compile

SYSROOT_DIRS += "/boot"
FILES:${PN} += "/boot/*.elf /boot/*.bin /boot/*.ub"
RPROVIDES:${PN} += "arm-trusted-firmware"

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
}
