PROVIDES += "qemu"

# Skip processing of this recipe if it is not explicitly specified as the
# PREFERRED_PROVIDER for qemu/nativesdk-qemu. This avoids network access required by
# the use of AUTOREV SRCREVs, which may be the default for some recipes.
python () {
    if bb.data.inherits_class('nativesdk', d):
        if (not d.getVar("PREFERRED_PROVIDER_nativesdk-qemu") and "nativesdk-qemu" != d.getVar("PN")) or \
           (d.getVar("PREFERRED_PROVIDER_nativesdk-qemu") and d.getVar("PREFERRED_PROVIDER_nativesdk-qemu") != d.getVar("PN")):
            d.setVar('BB_DONT_CACHE', '1')
            raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_nativesdk-qemu to %s to enable it" % (d.getVar("PN")))
    elif (not d.getVar("PREFERRED_PROVIDER_qemu") and "qemu" != d.getVar("PN")) or \
       (d.getVar("PREFERRED_PROVIDER_qemu") and d.getVar("PREFERRED_PROVIDER_qemu") != d.getVar("PN")):
        d.setVar('BB_DONT_CACHE', '1')
        raise bb.parse.SkipRecipe("Set PREFERRED_PROVIDER_qemu to %s to enable it" % (d.getVar("PN")))

    if (d.getVar('BPN') != 'qemu'):
        for pkg in d.getVar('PACKAGES').split():
            d.appendVar('RPROVIDES:%s' % pkg, ' ' + pkg.replace(d.getVar('PN'), 'qemu'))

        for pkg in d.getVar('PACKAGES_DYNAMIC').split():
            d.appendVar('PACKAGES_DYNAMIC', ' ' + pkg.replace(d.getVar('PN'), 'qemu'))
}

# From Poky qemu.inc
# Modified to add compatibility RPROVIDES
python split_qemu_packages () {
    archdir = d.expand('${bindir}/')
    syspackages = do_split_packages(d, archdir, r'^qemu-system-(.*)$', '${PN}-system-%s', 'QEMU full system emulation binaries(%s)' , prepend=True)
    if syspackages:
        # Ensure installing the main ${PN} package pulls in all of the
        # per-arch qemu-system-* subpackages (matches upstream qemu.inc
        # behavior). Previously this set RDEPENDS on a non-existent
        # "${PN}-system-all" package, which meant these subpackages were
        # never actually installed (e.g. missing qemu-system-aarch64 in
        # nativesdk-qemu-xilinx based SDKs).
        d.appendVar('RDEPENDS:' + d.getVar('PN'), ' ' + ' '.join(syspackages))

        if (d.getVar('BPN') != 'qemu'):
            for pkg in syspackages:
                d.appendVar('RPROVIDES:%s' % pkg, ' ' + pkg.replace(d.getVar('PN'), 'qemu'))

    userpackages = do_split_packages(d, archdir, r'^qemu-((?!system|edid|ga|img|io|nbd|pr-helper|storage-daemon).*)$', '${PN}-user-%s', 'QEMU full user emulation binaries(%s)' , prepend=True)
    if userpackages:
        # See comment above; pull in all qemu-user-* subpackages too.
        d.appendVar('RDEPENDS:' + d.getVar('PN'), ' ' + ' '.join(userpackages))

        if (d.getVar('BPN') != 'qemu'):
            for pkg in userpackages:
                d.appendVar('RPROVIDES:%s' % pkg, ' ' + pkg.replace(d.getVar('PN'), 'qemu'))

    mipspackage = d.getVar('PN') + "-user-mips"
    if mipspackage in ' '.join(userpackages):
        d.appendVar('RDEPENDS:' + mipspackage, ' ' + d.getVar("MLPREFIX") + 'bash')

        if (d.getVar('BPN') != 'qemu'):
            for pkg in mipspackage:
                d.appendVar('RPROVIDES:%s' % pkg, ' ' + pkg.replace(d.getVar('PN'), 'qemu'))
}

