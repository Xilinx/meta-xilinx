SUMMARY = "Recipe to download SDT artifacts and extract to directory"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PROVIDES = "virtual/sdt"

INHIBIT_DEFAULT_DEPS = "1"

inherit deploy image-artifact-names

# The user is expected to define SDT_URI, and SDT_URI[sha256sum].  Optionally
# they may also define SDT_URI[S] to define the unpacking path.
SDT_URI[doc] = "URI for the System Device Tree file(s), usually a tarball bundle of files"

# Add compatibility with previous gen-machine-conf output
SYSTEM_DTFILE_DIR ??= ""

SDT_URI ??= "${@'file://${SYSTEM_DTFILE_DIR}' if d.getVar('SYSTEM_DTFILE_DIR') else ''}"

SRC_URI = "${SDT_URI}"
SRC_URI[sha256sum] = "${@d.getVarFlag('SDT_URI', 'sha256sum') or 'undefined'}"

COMPATIBLE_HOST:xilinx-standalone = "${HOST_SYS}"
PACKAGE_ARCH ?= "${MACHINE_ARCH}"

# Don't set S = "${WORKDIR}/git" as we need this to work for other protocols
S = "${@d.getVarFlag('SDT_URI', 'S') or '${WORKDIR}'}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

python () {
    if not d.getVar('SDT_URI'):
        raise bb.parse.SkipRecipe("SDT_URI must be specified.  See recipe for instructions.")
}

do_install() {
    install -d ${D}${datadir}/sdt/${MACHINE}
    if [ "${S}" = "${WORKDIR}" ]; then
        # If we just copying everything, then we'll copy build components.
        # This fallback is for the case where the user provides each of the
        # files instead of a tarball.  It shouldn't be used, but is here just
        # in case.
        for files in ${S}/* ; do
            if [ -f $files ]; then
                cp --preserve=mode,timestamps $files ${D}${datadir}/sdt/${MACHINE}/.
            fi
        done
    else
        cp --preserve=mode,timestamps -R ${S}/* ${D}${datadir}/sdt/${MACHINE}/.
    fi
}

# Artifacts has plm.elf, psm.elf or other aie elf hence we need to strip and skip
# the packages.
INSANE_SKIP += "arch"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

FILES:${PN} = "${datadir}/sdt/${MACHINE}"

do_deploy() {
    install -d ${DEPLOYDIR}/system-dt${IMAGE_VERSION_SUFFIX}
    if [ "${S}" = "${WORKDIR}" ]; then
        # If we just copying everything, then we'll copy build components.
        # This fallback is for the case where the user provides each of the
        # files instead of a tarball.  It shouldn't be used, but is here just
        # in case.
        for files in ${S}/* ; do
            if [ -f $files ]; then
                cp --preserve=mode,timestamps $files ${DEPLOYDIR}/system-dt${IMAGE_VERSION_SUFFIX}/.
            fi
        done
    else
        cp --preserve=mode,timestamps -R ${S}/* ${DEPLOYDIR}/system-dt${IMAGE_VERSION_SUFFIX}/.
    fi
    ln -s system-dt${IMAGE_VERSION_SUFFIX} ${DEPLOYDIR}/system-dt
}

addtask deploy after do_install before do_build
