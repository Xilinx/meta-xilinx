SUMMARY = "Deploys a pre-built AMD Versal Programmable Device Image \
(base PDI)."
DESCRIPTION = "Recipe to deploy base pdi"

LICENSE = "CLOSED"

PROVIDES = "virtual/base-pdi"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:microblaze = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"
COMPATIBLE_MACHINE:versal-2vp = ".*"

COMPATIBLE_HOST = ".*"

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

do_compile[noexec] = "1"

BASE_PDI_DTFILE ??= "${@d.getVar('SYSTEM_DTFILE') or d.getVar('CONFIG_DTFILE') or ''}"

def base_pdi_vivado_version(d):
    import re
    dtfile = d.getVar("BASE_PDI_DTFILE")
    if not dtfile:
        return ""
    try:
        with open(dtfile) as f:
            for line in f:
                m = re.search(r'vivado-version\s*=\s*"([^"]+)"', line)
                if m:
                    return m.group(1)
    except OSError as e:
        bb.warn("base-pdi: cannot read %s: %s" % (dtfile, e))
    return ""

inherit shared-manifest

MANIFEST_COMPONENT_FIELDS = "vivado_version"
MANIFEST_COMPONENT_FIELD_vivado_version = "${@base_pdi_vivado_version(d)}"

do_install[file-checksums] += "${@'%s:%s' % (d.getVar('BASE_PDI_DTFILE'), str(os.path.exists(d.getVar('BASE_PDI_DTFILE')))) if d.getVar('BASE_PDI_DTFILE') else ''}"

PDI_PATH ?= ""
SRC_URI += "${@['file://'+d.getVar('PDI_PATH'),''][d.getVar('PDI_PATH') == '']}"

python() {
    if d.getVar('PDI_SKIP_CHECK') != "1" and not d.getVar('PDI_PATH'):
        raise bb.parse.SkipRecipe("PDI_PATH is not configured with the base design pdi")
}

#base install will just take from PDI_PATH variable
do_install() {

    if [ -f ${WORKDIR}/${PDI_PATH} ];then
        install -d ${D}/boot
        install -m 0644 ${WORKDIR}/${PDI_PATH} ${D}/boot/base-design.pdi
    else
        bbfatal "No base pdi supplied"
    fi
}
SYSROOT_DIRS += "/boot"

FILES:${PN} += "/boot/*"
