require recipes-security/optee/optee-os.inc

DEPENDS += "dtc-native"
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

BRANCH ?= ""
REPO ?= "git://github.com/Xilinx/optee_os.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

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

EXTRA_OEMAKE:append = " CFG_TEE_CORE_LOG_LEVEL=2 CFG_TEE_TA_LOG_LEVEL=2 CFG_DT=y"
