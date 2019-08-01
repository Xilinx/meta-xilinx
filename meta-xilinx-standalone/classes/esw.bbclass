inherit pkgconfig cmake yocto-cmake-translation

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${ESWS}/license.txt;md5=c83c24ed6555ade24e37e6b74ade2629"

XILINX_RELEASE_VERSION = "v2019.2"

# We should move to an actual SRCREV eventually
SRCREV = "${AUTOREV}"
PV = "${XILINX_RELEASE_VERSION}+git${SRCPV}"
SRC_URI = "git://gitenterprise.xilinx.com/appanad/decoupling_embeddedsw.git;branch=master;protocol=https"

SRCREV_FORMAT = "src_decouple"

ESWS = "${WORKDIR}/git/"
S = "${ESWS}/${ESW_COMPONENT_SRC}"


# We need to put these per recipe probably, e.g. pmu on mb, fsbl on a53
COMPATIBLE_HOST_zynqmp-pmu = "microblaze.*-elf"
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmp-pmu = "zynqmp-pmu"

COMPATIBLE_HOST_cortexa53 = "aarch64.*-elf"
COMPATIBLE_MACHINE_cortexa53 = "cortexa53"

cmake_do_generate_toolchain_file_append() {
    cat >> ${WORKDIR}/toolchain.cmake <<EOF
    include(CMakeForceCompiler)
    CMAKE_FORCE_C_COMPILER("${OECMAKE_C_COMPILER}" GNU)
    CMAKE_FORCE_CXX_COMPILER("${OECMAKE_CXX_COMPILER}" GNU)
EOF
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    install -m 0755  ${WORKDIR}/build/${ESW_COMPONENT_NAME} ${D}${libdir}
    install -m 0644  ${WORKDIR}/build/include/*.h ${D}${includedir}
}


# Should these be LDFLAGS?
CFLAGS_append = " -Os -flto -ffat-lto-objects"
