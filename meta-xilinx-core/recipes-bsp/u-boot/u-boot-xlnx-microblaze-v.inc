SRC_URI_RISCV = "\
    file://u-boot-riscv-isa_clear.cfg \
    ${@bb.utils.contains("TUNE_FEATURES", "a",      "file://u-boot-riscv-isa_a.cfg", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "c",      "file://u-boot-riscv-isa_c.cfg", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "f",      "file://u-boot-riscv-isa_f.cfg", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "d",      "file://u-boot-riscv-isa_d.cfg", "", d)} \
    ${@bb.utils.contains_any("TUNE_FEATURES", "b zbb",    "file://u-boot-riscv-isa_zbb.cfg", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "zicbom", "file://u-boot-riscv-isa_zicbom.cfg", "", d)} \
    "

UBOOT_MBV_RISCV_SRC = ""
UBOOT_MBV_RISCV_SRC:riscv64 = "${SRC_URI_RISCV}"

# Include microblaze-v-generic.cfg file for 2026.01 and later versions of
# u-boot-xlnx.
UBOOT_MBV_GENERIC_CFG = ""
UBOOT_MBV_GENERIC_CFG:microblaze-v = "file://microblaze-v-generic.cfg"

SRC_URI += "${UBOOT_MBV_RISCV_SRC} ${UBOOT_MBV_GENERIC_CFG}"
