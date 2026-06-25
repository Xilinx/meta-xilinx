FILESEXTRAPATHS:prepend := "${THISDIR}/linux-xlnx:"

LINUX_MB_INC = ""
LINUX_MB_INC:microblaze = "linux-microblaze.inc"
require ${LINUX_MB_INC}

# Zynq default generic KMACHINE
COMPATIBLE_MACHINE:zynq = "zynq"
KMACHINE:zynq = "zynq"

# ZynqMP default generic KMACHINE
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
KMACHINE:zynqmp = "zynqmp"

# MicroBlaze default generic KMACHINE
COMPATIBLE_MACHINE:microblaze = "microblaze"
KMACHINE:microblaze = "microblaze"

# MicroBlaze-v default generic KMACHINE
COMPATIBLE_MACHINE:riscv32 = "${MACHINE}"
KMACHINE:riscv32 = "${TUNE_RISCV_ARCH}"

COMPATIBLE_MACHINE:riscv64 = "${MACHINE}"
KMACHINE:riscv64 = "${TUNE_RISCV_ARCH}"

SRC_URI_RISCV = "\
    file://risc-v-isa-clear.cfg \
    ${@bb.utils.contains(    "TUNE_FEATURES", "rv 32 i m a", "file://risc-v-isa-rv32i.cfg", "", d)} \
    ${@bb.utils.contains(    "TUNE_FEATURES", "rv 64 i m a", "file://risc-v-isa-rv64i.cfg", "", d)} \
    ${@bb.utils.contains(    "TUNE_FEATURES", "f d",         "file://risc-v-isa-fpu.cfg",   "", d)} \
    ${@bb.utils.contains(    "TUNE_FEATURES", "c",           "file://risc-v-isa-c.cfg",     "", d)} \
    ${@bb.utils.contains(    "TUNE_FEATURES", "v",           "file://risc-v-isa-v.cfg",     "", d)} \
    ${@bb.utils.contains_any("TUNE_FEATURES", "b zbb",       "file://risc-v-isa-zbb.cfg",   "", d)} \
    ${@bb.utils.contains(    "TUNE_FEATURES", "zicbom",      "file://risc-v-isa-zicbom.cfg",   "", d)} \
    "

SRC_URI:append:riscv32 = "${SRC_URI_RISCV}"
SRC_URI:append:riscv64 = "${SRC_URI_RISCV}"
