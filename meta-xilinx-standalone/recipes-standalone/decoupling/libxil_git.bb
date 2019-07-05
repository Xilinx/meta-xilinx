inherit pkgconfig cmake yocto-cmake-translation deploy

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://../../license.txt;md5=c83c24ed6555ade24e37e6b74ade2629"

XILINX_RELEASE_VERSION = "v2019.1"

#SRCREV = "43d6e594666215e6f328376f21cb1c07fe57632f"
SRCREV = "${AUTOREV}"
PV = "${XILINX_RELEASE_VERSION}+git${SRCPV}"
SRC_URI = "git://gitenterprise.xilinx.com/appanad/decoupling_embeddedsw.git;branch=master;protocol=https"

COMPATIBLE_HOST = "microblaze.*-elf"
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmp-pmu = "zynqmp-pmu"

S = "${WORKDIR}/git/XilinxProcessorIPLib/drivers/"

DEPENDS += "dtc-native python3-pyyaml-native xilmem"

DTBFILE="output.dtb"
do_generate_cmake (){
    # This will generate CMakeLists.txt which contains
    # drivers for the libxil 
    cd ${WORKDIR}/git
    DRIVERS_LIST=$(nativepython3 ${WORKDIR}/git/scripts/getdrvlist.py -d ${WORKDIR}/git/dts_files/${DTBFILE})
}

do_generate_driver_data (){
    # This script should also not rely on relative paths and such
    cd ${WORKDIR}/git
    nativepython3 ${WORKDIR}/git/scripts/generate_drvdata.py -d ${WORKDIR}/git/dts_files/${DTBFILE}
}

do_create_dtb (){
    cd ${WORKDIR}
    # Which  dts are we supposed to use?
    # Should it include a dtsi?
    # Again this flow probably needs work
    # We should also use variables as input and output
    ${BUILD_CPP} -nostdinc -I ${WORKDIR}/git/dts_files/include -undef -x assembler-with-cpp ${WORKDIR}/git/dts_files/zynqmp-zcu102-rev1.0.dts > ${WORKDIR}/git/test.dts
    dtc -I dts -O dtb -b 0 -@ -o ${WORKDIR}/git/dts_files/output.dtb ${WORKDIR}/git/test.dts
}

addtask do_generate_cmake before do_configure after do_create_dtb

addtask do_generate_driver_data before do_compile after do_create_dtb

addtask do_create_dtb before do_compile after do_prepare_recipe_sysroot

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
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    install -m 0755  ${WORKDIR}/build/libxil.a ${D}${libdir}
    install -m 0644  ${WORKDIR}/build/include/* ${D}${includedir}
}
