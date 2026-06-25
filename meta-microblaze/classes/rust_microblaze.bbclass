python __anonymous() {
    if bb.data.inherits_class('rust-target-config', d):
        if d.getVar('TARGET_ARCH') in ['microblaze', 'microblazeel', 'microblazeeb']:
            raise bb.parse.SkipRecipe("Rust does not support microblaze.")
        # riscv32/riscv64 could be supported if not for MB-V.  Only the MB-V version is limited at this time.
        if d.getVar('TARGET_ARCH') in ['riscv32', 'riscv64'] and 'rv' in (d.getVar('TUNE_FEATURES') or '').split():
            raise bb.parse.SkipRecipe("Rust does not support microblaze-v at this time.")
}
