
# enable the overrides for the context of the conf only
OVERRIDES .= ":qemuboot-xilinx"

# Default machine targets for Xilinx QEMU (FDT Generic)
# Allow QB_MACHINE to be overridden by a BSP config
QB_MACHINE ?= "${QB_MACHINE_XILINX}"
QB_RNG=""
QB_MACHINE_XILINX:aarch64 = "-machine arm-generic-fdt"
QB_MACHINE_XILINX:arm = "-M arm-generic-fdt-7series"
QB_MACHINE_XILINX:microblaze = "-M microblaze-fdt-plnx"

# defaults
QB_DEFAULT_KERNEL ?= "none"
QB_DEFAULT_KERNEL:zynq ?= "${@'zImage' if \
		d.getVar('INITRAMFS_IMAGE_BUNDLE') != '1' else 'zImage-initramfs-${MACHINE}.bin'}"
QB_DEFAULT_KERNEL:microblaze ?= "${@'simpleImage.mb' if \
		d.getVar('INITRAMFS_IMAGE_BUNDLE') != '1' else 'simpleImage.mb-initramfs-${MACHINE}.bin'}"

inherit qemuboot

# rewrite the qemuboot with the custom sysroot bindir
python do_write_qemuboot_conf:append() {
    val = os.path.join(d.getVar('BASE_WORKDIR'), d.getVar('BUILD_SYS'), 'qemu-xilinx-helper-native/1.0-r1/recipe-sysroot-native/usr/bin/')
    cf.set('config_bsp', 'STAGING_BINDIR_NATIVE', '%s' % val)

    # write out the updated version from this append
    with open(qemuboot, 'w') as f:
        cf.write(f)
}

