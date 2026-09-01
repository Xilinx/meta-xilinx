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
