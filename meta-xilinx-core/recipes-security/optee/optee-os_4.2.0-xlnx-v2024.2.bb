require recipes-security/optee/optee-os.inc

DEPENDS += "dtc-native"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/Xilinx/optee/optee_os.git;branch=master;protocol=https"

SRCREV = "72af0f51b5604abbf177e767cf8fef6d2a9ecb16"

COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

OPTEEMACHINE:versal-2ve-2vm = "versal2"

OPTEE_ARCH ?= "arm64"

TOOLCHAIN = "gcc"

# Optee Console settings
# 0 for uart0/serial0 and 1 for uart1/serial1
OPTEE_CONSOLE_DEFAULT ?= ""
OPTEE_CONSOLE_DEFAULT:versal-2ve-2vm = "0"

OPTEE_CONSOLE ?= "${OPTEE_CONSOLE_DEFAULT}"

EXTRA_OEMAKE:append = "${@' CFG_CONSOLE_UART=${OPTEE_CONSOLE}' if d.getVar('OPTEE_CONSOLE', True) != '' else ''}"

EXTRA_OEMAKE:append = " CFG_TEE_CORE_LOG_LEVEL=2 CFG_TEE_TA_LOG_LEVEL=2"
