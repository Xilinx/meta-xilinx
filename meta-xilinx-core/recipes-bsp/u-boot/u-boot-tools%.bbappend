# Skip processing of this recipe if it is not explicitly specified as the
# PREFERRED_PROVIDER for u-boot-tools. This avoids network access required by
# the use of AUTOREV SRCREVs, which may be the default for some recipes.
python () {
    if bb.data.inherits_class('native', d):
        if (not d.getVar("PREFERRED_PROVIDER_u-boot-tools-native") and "u-boot-tools-native" != d.getVar("PN")) or \
           (d.getVar("PREFERRED_PROVIDER_u-boot-tools-native") and d.getVar("PREFERRED_PROVIDER_u-boot-tools-native") != d.getVar("PN")):
            d.delVar("BB_DONT_CACHE")
            raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_u-boot-tools-native to %s to enable it" % (d.getVar("PN")))

    elif bb.data.inherits_class('nativesdk', d):
        if (not d.getVar("PREFERRED_PROVIDER_nativesdk-u-boot-tools") and "nativesdk-u-boot-tools" != d.getVar("PN")) or \
           (d.getVar("PREFERRED_PROVIDER_nativesdk-u-boot-tools") and d.getVar("PREFERRED_PROVIDER_nativesdk-u-boot-tools") != d.getVar("PN")):
            d.delVar("BB_DONT_CACHE")
            raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_nativesdk-u-boot-tools to %s to enable it" % (d.getVar("PN")))

    else:
        if (not d.getVar("PREFERRED_PROVIDER_u-boot-tools") and "u-boot-tools" != d.getVar("PN")) or \
           (d.getVar("PREFERRED_PROVIDER_u-boot-tools") and d.getVar("PREFERRED_PROVIDER_u-boot-tools") != d.getVar("PN")):
            d.delVar("BB_DONT_CACHE")
            raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_u-boot-tools to %s to enable it" % (d.getVar("PN")))
}

