# These items are all potentially machine specific, and should not be
# generically applied to u-boot-tools-xlnx.
SRC_URI:append:versal-2ve-2vm = "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'file://u-boot-optee.cfg', '', d)} \
    "

# EFI serial console size auto-detect sends an ANSI CPR query (ESC[6n). On serial
# consoles the terminal's reply (ESC[<rows>;<cols>R) can arrive after U-Boot's
# 100ms read window and leak into the shell, parsed as bogus commands. Disable
# the ANSI query for EFI-enabled machines.
SRC_URI:append = " ${@bb.utils.contains('MACHINE_FEATURES', 'efi', 'file://u-boot-efi-disable-ansi.cfg', '', d)}"

SRC_URI_RISCV = "\
    file://u-boot-riscv-isa_clear.cfg \
    ${@bb.utils.contains("TUNE_FEATURES", "a",      "file://u-boot-riscv-isa_a.cfg", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "c",      "file://u-boot-riscv-isa_c.cfg", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "f",      "file://u-boot-riscv-isa_f.cfg", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "d",      "file://u-boot-riscv-isa_d.cfg", "", d)} \
    ${@bb.utils.contains_any("TUNE_FEATURES", "b zbb",    "file://u-boot-riscv-isa_zbb.cfg", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "zicbom", "file://u-boot-riscv-isa_zicbom.cfg", "", d)} \
    "

SRC_URI:append:riscv32 = "${SRC_URI_RISCV}"
SRC_URI:append:riscv64 = "${SRC_URI_RISCV}"

# Include microblaze-v-generic.cfg file for 2026.01 and later versions of
# u-boot-xlnx.
SRC_URI:append:microblaze-v = "\
    file://microblaze-v-generic.cfg \
    "
