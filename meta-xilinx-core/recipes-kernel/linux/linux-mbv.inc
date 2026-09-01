# MicroBlaze-V (MicroBlaze RISC-V) is only compatible with the riscv64
# tune/arch (32-bit MB-V Linux support has been dropped).
COMPATIBLE_MACHINE:riscv64 = ".*"

# Use DEFCONFIGs for configuring linux-xlnx kernels
KBUILD_DEFCONFIG_DEFAULT:riscv64 ?= "defconfig"

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

SRC_URI:append:riscv64 = "${SRC_URI_RISCV}"

# kernel-yocto.bbclass supports resolving KERNEL_FEATURES entries against
# in-tree config fragments (arch/${ARCH}/configs and kernel/configs), see:
# https://git.openembedded.org/openembedded-core/commit/meta/classes-recipe/kernel-yocto.bbclass?h=wrynose&id=e4e50a9a93d606ba5fef8f10f1ec20deaed7ab9c
# This lets us reference linux-xlnx/arch/riscv/configs/64-bit.config
# and amd.config directly, instead of keeping local riscv-64-bit.cfg
# and riscv-amd.cfg copies in sync with the kernel source via a
# separate do_compare_config_files sanity check.
KERNEL_FEATURES:append:riscv64 = " 64-bit.config"
KERNEL_FEATURES:append = " amd.config"

# mbv-amd-override.cfg overrides some configs set in
# linux-xlnx/arch/riscv/configs/amd.config above, so it still needs to be
# fetched via SRC_URI (it is only maintained in this layer, not in-tree).
SRC_URI:append = "\
    file://mbv-amd-override.cfg \
    "

# Fragments fetched via SRC_URI are always merged before entries listed in
# KERNEL_FEATURES, so amd.config above would otherwise be applied *after*
# mbv-amd-override.cfg and clobber the override. Listing
# mbv-amd-override.cfg here too, after amd.config, guarantees it is
# applied last and its settings win.
KERNEL_FEATURES:append = " mbv-amd-override.cfg"

# Config required for systemd boot.
KERNEL_FEATURES:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' features/microblaze-v-systemd/microblaze-v-systemd.scc', '', d)} \
    "
