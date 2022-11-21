PROVIDES += "qemu-system-native"

# Skip processing of this recipe if it is not explicitly specified as the
# PREFERRED_PROVIDER for qemu-syste-native. This avoids network access required by
# the use of AUTOREV SRCREVs, which may be the default for some recipes.
python () {
    if (not d.getVar("PREFERRED_PROVIDER_qemu-system-native") and "qemu-system-native" != d.getVar("PN")) or \
       (d.getVar("PREFERRED_PROVIDER_qemu-system-native") and d.getVar("PREFERRED_PROVIDER_qemu-system-native") != d.getVar("PN")):
        d.delVar("BB_DONT_CACHE")
        raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_qemu-system-native to %s to enable it" % (d.getVar("PN")))
}

RPROVIDES:${PN} += "qemu-system-native"
