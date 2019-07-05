inherit pkgconfig cmake yocto-cmake-translation deploy

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://../../../../license.txt;md5=c83c24ed6555ade24e37e6b74ade2629"

XILINX_RELEASE_VERSION = "v2019.1"
DEPENDS = " xilstandalone xilfpga xilskey"

SRCREV = "${AUTOREV}"
PV = "${XILINX_RELEASE_VERSION}+git${SRCPV}"
SRC_URI = "git://gitenterprise.xilinx.com/appanad/decoupling_embeddedsw.git;branch=master;protocol=https"

COMPATIBLE_HOST = "microblaze.*-elf"
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmp-pmu = "zynqmp-pmu"

S = "${WORKDIR}/git/lib/sw_apps/zynqmp_pmufw/src"

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

do_install() {
    :
}

PMU_FIRMWARE_BASE_NAME ?= "${BPN}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}"
PMU_FIRMWARE_BASE_NAME[vardepsexclude] = "DATETIME"

do_deploy() {
    install -Dm 0644 ${WORKDIR}/build/pmufw ${DEPLOYDIR}/${PMU_FIRMWARE_BASE_NAME}.elf
    ln -sf ${PMU_FIRMWARE_BASE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}.elf
    ${OBJCOPY} -O binary ${WORKDIR}/build/pmufw ${WORKDIR}/build/pmfw.bin
    install -m 0644 ${WORKDIR}/build/pmufw.bin ${DEPLOYDIR}/${PMU_FIRMWARE_BASE_NAME}.bin
    ln -sf ${PMU_FIRMWARE_BASE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}.bin
}

