# These items are all potentially machine specific, and should not be
# generically applied to u-boot-tools-xlnx.
UBOOT_ARCH_2VE_2VM_SRC = ""
UBOOT_ARCH_2VE_2VM_SRC:versal-2ve-2vm = "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'optee', 'file://u-boot-optee.cfg', '', d)} \
    "

SRC_URI += "${UBOOT_ARCH_2VE_2VM_SRC}"

# EFI serial console size auto-detect sends an ANSI CPR query (ESC[6n). On serial
# consoles the terminal's reply (ESC[<rows>;<cols>R) can arrive after U-Boot's
# 100ms read window and leak into the shell, parsed as bogus commands. Disable
# the ANSI query for EFI-enabled machines.
SRC_URI += "${@bb.utils.contains('MACHINE_FEATURES', 'efi', 'file://u-boot-efi-disable-ansi.cfg', '', d)}"

# MicroBlaze-V (MicroBlaze RISC-V) include file.
UBOOT_MBV_INC = ""
UBOOT_MBV_INC:microblaze-v = "u-boot-xlnx-microblaze-v.inc"
require ${UBOOT_MBV_INC}
