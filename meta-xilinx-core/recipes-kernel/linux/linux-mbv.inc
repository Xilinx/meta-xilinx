# Yocto KBUILD_DEFCONFIG doesn't support passing multiple config files hence we
# want to copy 32-bit.config, 64-bit.config and amd.config file as fragments and
# append using SRC_URI.

# Copy of linux-xlnx/arch/riscv/configs/32-bit.config as riscv-32-bit.cfg
SRC_URI:append:riscv32 = "\
    file://riscv-32-bit.cfg \
    "

# Copy of linux-xlnx/arch/riscv/configs/64-bit.config as riscv-64-bit.cfg
SRC_URI:append:riscv64 = "\
    file://riscv-64-bit.cfg \
    "

# Copy of linux-xlnx/arch/riscv/configs/amd.config as riscv-amd.cfg.
# Also mbv-amd-override.cfg is to override some configs set in
# linux-xlnx/arch/riscv/configs/amd.config
SRC_URI:append = "\
    file://riscv-amd.cfg \
    file://mbv-amd-override.cfg \
    "

python do_compare_config_files() {
    import filecmp
    kernel_source_dir = d.getVar('S')
    kernel_work_dir = d.getVar('WORKDIR')

    linux_32bit_config = os.path.join(kernel_source_dir + '/arch/riscv/configs/32-bit.config')
    linux_64bit_config = os.path.join(kernel_source_dir + '/arch/riscv/configs/64-bit.config')
    linux_amd_config = os.path.join(kernel_source_dir + '/arch/riscv/configs/amd.config')

    yocto_32bit_config = os.path.join(kernel_work_dir + '/riscv-32-bit.cfg')
    yocto_64bit_config = os.path.join(kernel_work_dir + '/riscv-64-bit.cfg')
    yocto_amd_config = os.path.join(kernel_work_dir + '/riscv-amd.cfg')

    # Compare linux-xlnx/arch/riscv/configs/32-bit.config with ${WORKDIR}/riscv-32-bit.cfg
    if os.path.isfile(linux_32bit_config) and os.path.isfile(yocto_32bit_config):
        if not filecmp.cmp(linux_32bit_config, yocto_32bit_config):
            bb.fatal("Linux repo Kernel Config Fragment: %s doesn't match with Yocto Config Fragment: %s. \
                Sync riscv-32-bit.cfg with linux-xlnx/arch/riscv/configs/32-bit.config"
                % (linux_32bit_config, yocto_32bit_config))
        else:
            bb.debug(2, "Linux repo Kernel Config Fragment: %s matches with Yocto Config Fragment: %s"
                % (linux_32bit_config, yocto_32bit_config))

    # Compare linux-xlnx/arch/riscv/configs/64-bit.config with ${WORKDIR}/riscv-64-bit.cfg
    if os.path.isfile(linux_64bit_config) and os.path.isfile(yocto_64bit_config):
        if not filecmp.cmp(linux_64bit_config, yocto_64bit_config):
            bb.fatal("Linux repo Kernel Config Fragment: %s doesn't match with Yocto Config Fragment: %s. \
                Sync riscv-64-bit.cfg with linux-xlnx/arch/riscv/configs/64-bit.config"
                % (linux_64bit_config, yocto_64bit_config))
        else:
            bb.debug(2, "Linux repo Kernel Config Fragment: %s matches with Yocto Config Fragment: %s"
                % (linux_64bit_config, yocto_64bit_config))

    # Compare linux-xlnx/arch/riscv/configs/amd.config with ${WORKDIR}/riscv-amd.cfg
    if os.path.isfile(linux_amd_config) and os.path.isfile(yocto_amd_config):
        if not filecmp.cmp(linux_amd_config, yocto_amd_config):
            bb.fatal("Linux repo Kernel Config Fragment: %s doesn't match with Yocto Config Fragment: %s. \
                Sync riscv-amd.cfg with linux-xlnx/arch/riscv/configs/amd.config"
                % (linux_amd_config, yocto_amd_config))
        else:
            bb.debug(2, "Linux repo Kernel Config Fragment: %s matches with Yocto Config Fragment: %s"
                % (linux_amd_config, yocto_amd_config))
}

do_kernel_configme[prefuncs] += "do_compare_config_files"

# Config required for systemd boot.
KERNEL_FEATURES:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' features/microblaze-v-systemd/microblaze-v-systemd.scc', '', d)} \
    "
