require plm-firmware.inc

# Separate build directories for versal and versal-net
SOC_DIR = "versal"
SOC_DIR:versal-net = "versal_net"
B = "${S}/lib/sw_apps/versal_plm/src/${SOC_DIR}"

BSP_DIR ?= "${B}/../../misc/versal_plm_bsp"

FILESPATH .= ":${FILE_DIRNAME}/embeddedsw/2023.1:${FILE_DIRNAME}/embeddedsw"

SRC_URI += " \
            file://makefile-skip-copy_bsp.sh.patch \
            file://0001-versal_fw-Fixup-core-makefiles.patch \
            file://0001-Workaround-Disable-Wnull-dereference.patch \
           "

EXTRA_COMPILER_FLAGS = "-g -ffunction-sections -fdata-sections -Wall -Wextra -Os -flto -ffat-lto-objects"

# Workaround for: ../../../include/xparameters.h:1021:67: warning: conversion from 'long long unsigned int' to 'unsigned int' changes value from '18446744073709551615' to '4294967295' [-Woverflow]
EXTRA_COMPILER_FLAGS += "-Wno-overflow"

# Workaround for: xpm_domain_iso.c:724:42: error: potential null pointer dereference [-Werror=null-dereference]
EXTRA_COMPILER_FLAGS += "-Wno-null-dereference"

do_configure() {
    # manually do the copy_bsp step first, so as to be able to fix up use of
    # mb-* commands
    ${B}/../../misc/${SOC_DIR}/copy_bsp.sh
}

do_compile() {
    pwd
    oe_runmake

    ${MB_OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/${ESW_COMPONENT}.bin
}
