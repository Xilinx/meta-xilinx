SUMMARY = "Zephyr SDK Bundle"
DESCRIPTION = "Pre-built Zephyr SDK bundle (compilers, libraries and \
CMake toolchain files for every supported Zephyr target architecture) \
produced by the Zephyr project's crosstool-NG build."
COMPATIBLE_HOST = "(x86_64|aarch64).*-linux"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

INHIBIT_DEFAULT_DEPS = "1"
# CMake is required by the setup script
DEPENDS += "cmake"

SDK_ARCHIVE = "zephyr-sdk-${PV}_linux-${BUILD_ARCH}.tar.xz"
SDK_NAME = "${BUILD_ARCH}"
SRC_URI = "https://github.com/zephyrproject-rtos/sdk-ng/releases/download/v${PV}/${SDK_ARCHIVE};subdir=${S};name=${SDK_NAME}"

SRC_URI[x86_64.sha256sum] = "0ae4b19ca034cec2ce2e88b76247e295f1f9a011078dd71425a02c7fd2460008"
SRC_URI[aarch64.sha256sum] = "f41ed9eb8cdcaff8393fecbc7a49d76b4322288b0ac42c09eec8659dd164dfc9"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

ZEPHYR_SDK_DIR = "${prefix}/zephyr-sdk"

do_install() {
    install -d ${D}${prefix}
    cp -r ${S}/zephyr-sdk-${PV} ${D}${ZEPHYR_SDK_DIR}

    # Install host tools
    ${D}${ZEPHYR_SDK_DIR}/setup.sh -h
}

SYSROOT_DIRS += "${ZEPHYR_SDK_DIR}"
INHIBIT_SYSROOT_STRIP = "1"
BBCLASSEXTEND = "native"
