python __anonymous() {
    if bb.data.inherits_class('image-oci', d):
        if d.getVar('TARGET_ARCH') in ['microblaze', 'microblazeel', 'microblazeeb']:
            raise bb.parse.SkipRecipe("Image OCI does not support microblaze.")
        # riscv32 is not supported for MB-V.
        if d.getVar('TARGET_ARCH') in ['riscv32']:
            raise bb.parse.SkipRecipe("Image OCI does not support microblaze-v at this time.")
}
