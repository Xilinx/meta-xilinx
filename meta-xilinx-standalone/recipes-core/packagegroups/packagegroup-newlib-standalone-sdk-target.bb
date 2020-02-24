COMPATIBLE_OS_xilinx-standalone = "${TARGET_OS}"

SUMMARY = "Target packages for the standalone SDK"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

RDEPENDS_${PN} = "\
    libgcc-dev \
    libstdc++-dev \
    ${LIBC_DEPENDENCIES} \
    "
