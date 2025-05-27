python do_configure () {
    externalsrc = d.getVar('EXTERNALSRC')
    if externalsrc:
        dest = d.getVar('SHARED_S')
        if externalsrc != dest:
            oe.path.copyhardlinktree(externalsrc, dest)
            for symlink in d.getVar('EXTERNALSRC_SYMLINKS').split():
                link = symlink.split(':')[0]
                if os.path.islink((dest + '/' + link)):
                    os.unlink((dest + '/' + link))
}

deltask do_compile
deltask do_install
deltask do_populate_sysroot
deltask do_populate_lic
RM_WORK_EXCLUDE += "${PN}"

inherit xlnx-embeddedsw nopackages

COMPATIBLE_HOST = ".*"
COMPATIBLE_MACHINE = ".*"

PN = "embeddedsw-source-${ESW_VER}"
WORKDIR = "${TMPDIR}/work-shared/embeddedsw-${ESW_VER}"
SSTATE_SWSPEC = "sstate:embeddedsw::${ESW_VER}:r0::${SSTATE_VERSION}:"

STAMP = "${STAMPS_DIR}/work-shared/embeddedsw-${PV}-${PR}"
STAMPCLEAN = "${STAMPS_DIR}/work-shared/embeddedsw-${PV}-*"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = ""
PACKAGES = ""
TARGET_ARCH = "allarch"
TARGET_AS_ARCH = "none"
TARGET_CC_ARCH = "none"
TARGET_LD_ARCH = "none"
TARGET_OS = "linux"
baselib = "lib"
PACKAGE_ARCH = "all"

S = "${SHARED_S}"
B = "${WORKDIR}/build"
