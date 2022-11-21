PROVIDES += "qemu"

# Skip processing of this recipe if it is not explicitly specified as the
# PREFERRED_PROVIDER for qemu-syste-native. This avoids network access required by
# the use of AUTOREV SRCREVs, which may be the default for some recipes.
python () {
    if bb.data.inherits_class('nativesdk', d):
        if (not d.getVar("PREFERRED_PROVIDER_nativesdk-qemu") and "qemu" != d.getVar("PN")) or \
           (d.getVar("PREFERRED_PROVIDER_nativesdk-qemu") and d.getVar("PREFERRED_PROVIDER_nativesdk-qemu") != d.getVar("PN")):
            d.delVar("BB_DONT_CACHE")
            raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_nativesdk-qemu to %s to enable it" % (d.getVar("PN")))
    elif (not d.getVar("PREFERRED_PROVIDER_qemu") and "qemu" != d.getVar("PN")) or \
       (d.getVar("PREFERRED_PROVIDER_qemu") and d.getVar("PREFERRED_PROVIDER_qemu") != d.getVar("PN")):
        d.delVar("BB_DONT_CACHE")
        raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_qemu to %s to enable it" % (d.getVar("PN")))
}

RPROVIDES:${PN} += "qemu"
RPROVIDES:${PN}-ptest += "qemu-ptest"
