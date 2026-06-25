SUMMARY = "AMD Xilinx Embedded Software (embeddedsw) source tree."
DESCRIPTION = "Source-only recipe that fetches the AMD Xilinx Embedded \
Software (embeddedsw) git tree and exports it for the per-PN ESW \
driver, library and firmware recipes to consume via shared-S. Not \
built or deployed on its own."

# Somewhere we need to verify the LIC_FILES_CHKSUM
# normally part of deployment, but there is no deployment
do_patch[postfuncs] += "populate_lic_qa_checksum"

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

addtask configure before do_build after do_patch
addtask patch before do_build after do_unpack

RM_WORK_EXCLUDE += "${PN}"

inherit xlnx-embeddedsw nopackages allarch

COMPATIBLE_HOST = ".*"
COMPATIBLE_MACHINE = ".*"

PN = "embeddedsw-source-${ESW_VER}"
WORKDIR = "${TMPDIR}/work-shared/embeddedsw-${ESW_VER}"
SSTATE_SWSPEC = "sstate:embeddedsw::${ESW_VER}:r0::${SSTATE_VERSION}:"

STAMP = "${STAMPS_DIR}/work-shared/embeddedsw-${PV}-${PR}"
STAMPCLEAN = "${STAMPS_DIR}/work-shared/embeddedsw-${PV}-*"

DEPENDS = ""
PACKAGES = ""
baselib = "lib"
PACKAGE_ARCH = "all"

S = "${SHARED_S}"
B = "${WORKDIR}/build"
