deltask do_configure
deltask do_compile
deltask do_install
deltask do_populate_sysroot
deltask do_populate_lic
RM_WORK_EXCLUDE += "${PN}"

inherit xlnx-embeddedsw nopackages

COMPATIBLE_HOST = ".*"
COMPATIBLE_MACHINE = ".*"

PN = "embeddedsw-source-${PV}"
WORKDIR = "${TMPDIR}/work-shared/embeddedsw-${PV}-${PR}"
SSTATE_SWSPEC = "sstate:embeddedsw::${PV}:${PR}::${SSTATE_VERSION}:"

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

UNPACKDIR = "${WORKDIR}/source"
S = "${SHARED_S}"
B = "${WORKDIR}/build"
