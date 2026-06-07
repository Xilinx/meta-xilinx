python __anonymous() {
    if bb.data.inherits_class('rust-target-config', d):
        if d.getVar('TARGET_ARCH') in ['microblaze', 'microblazeel', 'microblazeeb']:
            raise bb.parse.SkipRecipe("Rust does not support microblaze.")
}
