inherit cmake ccmake
 
COMPATIBLE_HOST = "aarch64-xilinx-elf"

require ../../meta-xilinx-core/recipes-bsp/ai-engine/aie-rt-2024.2.inc

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"
 
EXTRA_OECMAKE += "-DYOCTO=ON"
DEPENDS += "xilstandalone xiltimer"
 
ESW_COMPONENT_SRC = "/driver/src/"
ESW_COMPONENT_NAME = "libaienginev2.a"
 
OECMAKE_SOURCEPATH = "${S}/${ESW_COMPONENT_SRC}"
XLNX_CMAKE_SYSTEM_NAME ?= "Generic"
XLNX_CMAKE_BSP_VARS ?= ""

cmake_do_generate_toolchain_file:append() {
    cat >> ${WORKDIR}/toolchain.cmake <<EOF
    include(CMakeForceCompiler)
    CMAKE_FORCE_C_COMPILER("${OECMAKE_C_COMPILER}" GNU)
    CMAKE_FORCE_CXX_COMPILER("${OECMAKE_CXX_COMPILER}" GNU)
    set( CMAKE_SYSTEM_NAME "${XLNX_CMAKE_SYSTEM_NAME}" )
    set( CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${S}/cmake)
    set( CMAKE_LIBRARY_PATH ${B})
    add_definitions( "${XLNX_CMAKE_BSP_VARS} -DSDT" )
EOF
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    install -m 0755  ${B}/${ESW_COMPONENT_NAME} ${D}${libdir}
    install -m 0644  ${B}/include/*.h ${D}${includedir}
    cp -r ${B}/include/xaiengine ${D}${includedir}
}

