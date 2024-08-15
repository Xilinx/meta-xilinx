SUMMARY = "Recipe to download SDT artifacts and extract to directory"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INHIBIT_DEFAULT_DEPS = "1"

BB_STRICT_CHECKSUM = "${VALIDATE_SDT_CHECKSUM}"

SDT_MACHINE ??= "${@(d.getVar('MACHINE') or '')}"
SDT_MACHINE[doc] = "By defaul it is set to MACHINE, but can user change"

SDT_PROT ??= ""
SDT_PROT[doc] = "Download protocol method such as file://, git://, http:// or https://"

SDT_PATH ??= ""
SDT_PATH[doc] = "Path to https or http url or git repository or local file"

SDT_NAME ??= ""
SDT_NAME[doc] = "Path to the SDT output directory once downloaded, usually set by the recipe"

SDT_FILE_NAME ??= ""
SDT_FILE_NAME[doc] = "SDT File name such as <file-name>.tar.gz excluding extension"

BRANCH ??= ""
SRCREV ??= ""
SDT_BRANCHARG ??= "${@['', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SDT_BRANCHARGS = "${@['', ';${SDT_BRANCHARG}'][d.getVar('SDT_BRANCHARG', True) != '']}"

SDT_VERSION ??= "${@(d.getVar('XILINX_RELEASE_VERSION') or 'undefined').replace('v', '')}"
SDT_VERSION[doc] = "SDT artifacts release version"

# Provide a way to extend the SRC_URI, default to adding protocol=https for git:// usage.
SDT_EXTENSION ?= "${@';protocol=https' if d.getVar('SDT_PROT') == 'git://' else ''}"

PV = "${SDT_VERSION}"

SRC_URI = "${SDT_PROT}${SDT_PATH}${SDT_BRANCHARGS}${SDT_EXTENSION}"

include sdt-artifacts.inc

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

COMPATIBLE_HOST:xilinx-standalone = "${HOST_SYS}"

# Don't set S = "${WORKDIR}/git" as we need this to work for other protocols
# SDT_NAME will be adjusted to include /git if needed
S = "${WORKDIR}"

python do_fetch() {
    src_uri = (d.getVar('SRC_URI') or "").split()
    if not src_uri:
        return

    try:
        for uri in src_uri:
            if uri.startswith("file://"):
                import shutil
                fn = uri.split("://")[1].split(";")[0]
                shutil.copy(fn, os.path.join(d.getVar('DL_DIR')))
            else:
                fetcher = bb.fetch2.Fetch([uri], d)
                fetcher.download()
    except bb.fetch2.BBFetchException as e:
        bb.fatal("Bitbake Fetcher Error: " + repr(e))
}

python do_unpack() {
    src_uri = (d.getVar('SRC_URI') or "").split()
    if not src_uri:
        return

    try:
        for uri in src_uri:
            if uri.startswith("file://"):
                fn = uri.split("://")[1].split(";")[0]
                local_uri = "file://" + os.path.join(d.getVar('DL_DIR'))
            else:
                local_uri = uri

            fetcher = bb.fetch2.Fetch([local_uri], d)
            fetcher.unpack(d.getVar('WORKDIR'))
    except bb.fetch2.BBFetchException as e:
        bb.fatal("Bitbake Fetcher Error: " + repr(e))
}

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {

    if [ -d ${S}/${SDT_FILE_NAME} ]; then
        install -d ${D}${datadir}/sdt/${SDT_MACHINE}/${SDT_FILE_NAME}
        cp --preserve=mode,timestamps -R ${S}/${SDT_FILE_NAME}/* ${D}${datadir}/sdt/${SDT_MACHINE}/${SDT_FILE_NAME}
    else
        bbwarn "SDT artifacts expected but not found"
    fi
}

# Artifacts has plm.elf, psm.elf or other aie elf hence we need to strip and skip
# the packages.
INSANE_SKIP += "arch"
INHIBIT_SYSROOT_STRIP = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

FILES:${PN} = "${datadir}/sdt/${SDT_MACHINE}/${SDT_FILE_NAME}/*"
