# These items are all potentially machine specific, and should not be
# generically applied to u-boot-tools-xlnx.
SRC_URI:append:versal-2ve-2vm = "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'file://u-boot-optee.cfg', '', d)} \
    file://0001-amd_versal_2ve_2vm-Add-ufs-distro-boot-command.patch \
    "

SRC_URI_RISCV = "\
    file://u-boot-riscv-isa_clear.cfg \
    ${@bb.utils.contains("TUNE_FEATURES", "a",      "file://u-boot-riscv-isa_a.cfg", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "f",      "file://u-boot-riscv-isa_f.cfg", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "d",      "file://u-boot-riscv-isa_d.cfg", "", d)} \
    ${@bb.utils.contains_any("TUNE_FEATURES", "b zbb",    "file://u-boot-riscv-isa_zbb.cfg", "", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "zicbom", "file://u-boot-riscv-isa_zicbom.cfg", "", d)} \
    "

SRC_URI:append:riscv32 = "${SRC_URI_RISCV}"
SRC_URI:append:riscv64 = "${SRC_URI_RISCV}"
