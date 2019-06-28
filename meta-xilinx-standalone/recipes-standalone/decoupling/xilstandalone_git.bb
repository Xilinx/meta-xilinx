inherit pkgconfig cmake yocto-cmake-translation deploy

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://../../../../license.txt;md5=c83c24ed6555ade24e37e6b74ade2629"

XILINX_RELEASE_VERSION = "v2019.1"
DEPENDS = " libgloss"

#SRCREV = "43d6e594666215e6f328376f21cb1c07fe57632f"
SRCREV = "${AUTOREV}"
PV = "${XILINX_RELEASE_VERSION}+git${SRCPV}"
SRC_URI = "git://gitenterprise.xilinx.com/appanad/decoupling_embeddedsw.git;branch=master;protocol=https"

COMPATIBLE_HOST = "microblaze.*-elf"
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmp-pmu = "zynqmp-pmu"

S = "${WORKDIR}/git/lib/bsp/standalone/src/"

SRCREV_FORMAT = "src_decouple"

cmake_do_generate_toolchain_file_append() {
    cat >> ${WORKDIR}/toolchain.cmake <<EOF
    include(CMakeForceCompiler)
    CMAKE_FORCE_C_COMPILER("${OECMAKE_C_COMPILER}" GNU)
    CMAKE_FORCE_CXX_COMPILER("${OECMAKE_CXX_COMPILER}" GNU)
    set( CMAKE_C_FLAGS "${OECMAKE_C_FLAGS}" CACHE STRING "CFLAGS" )
    set( CMAKE_C_LINK_FLAGS "${OECMAKE_C_LINK_FLAGS}" CACHE STRING "LDFLAGS" )
EOF
}
