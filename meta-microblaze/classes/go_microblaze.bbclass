python __anonymous() {
    if bb.data.inherits_class('goarch', d):
        if d.getVar('TARGET_ARCH') in ['microblaze', 'microblazeel', 'microblazeeb']:
            raise bb.parse.SkipRecipe("Go does not support microblaze.")
}
