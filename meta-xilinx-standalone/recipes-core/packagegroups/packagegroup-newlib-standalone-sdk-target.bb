COMPATIBLE_HOST = "${HOST_SYS}"

SUMMARY = "Target packages for the standalone SDK"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

RDEPENDS:${PN} = "\
    libgcc-dev \
    libstdc++-dev \
    ${LIBC_DEPENDENCIES} \
    "
