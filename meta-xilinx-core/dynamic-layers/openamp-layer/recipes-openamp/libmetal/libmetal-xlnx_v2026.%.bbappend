inherit cmake ccmake deploy

OECMAKE_SOURCEPATH:xilinx-standalone = "${S}/"
PROVIDES:xilinx-standalone = "libmetal"

OECMAKE_SOURCEPATH:xilinx-freertos = "${S}/"
PROVIDES:xilinx-freertos = "libmetal libmetal-xlnx"

RPU_DEPENDS:append = " doxygen-native device-tree xiltimer ttcps libxil xilstandalone xiltimer ipipsu scugic xilpm "
DEPENDS:append:xilinx-standalone = " ${RPU_DEPENDS} "
DEPENDS:append:xilinx-freertos = " freertos10-xilinx "

DEPENDS:remove:microblaze = " scugic xiltimer "
DEPENDS:remove:armv7r = " sysfsutils eudev "
DEPENDS:remove:armv8r = " sysfsutils eudev "

COMPATIBLE_HOST:forcevariable = ".*"

LIBMETAL_CROSS_PREFIX:xilinx-standalone = "${TARGET_PREFIX}"
LIBMETAL_CROSS_PREFIX:xilinx-freertos = "${TARGET_PREFIX}"

LIBMETAL_MACHINE:armv7r = "xlnx_r5"
LIBMETAL_MACHINE:armv8r = "xlnx_r5"
LIBMETAL_MACHINE:microblaze = "microblaze_generic"
LIBMETAL_MACHINE:linux = "xlnx"

LIBMETAL_SYSTEM_NAME:xilinx-standalone = "generic"
LIBMETAL_SYSTEM_NAME:xilinx-freertos = "freertos"
LIBMETAL_SYSTEM_NAME:linux = "linux"

LIBMETAL_CMAKE_SYSTEM_PROCESSOR:xilinx-standalone = "${TARGET_ARCH}"
LIBMETAL_CMAKE_SYSTEM_PROCESSOR:xilinx-freertos = "${TARGET_ARCH}"
LIBMETAL_CMAKE_SYSTEM_PROCESSOR:xilinx-standalone:microblaze = "microblaze"

LIBMETAL_DEMO ?= "OFF"

EXTRA_OECMAKE = "\
    -DWITH_EXAMPLES=${LIBMETAL_DEMO} \
    "

CFLAGS:append:xilinx-standalone = " -DSDT ${DEBUG_PREFIX_MAP} -fno-pie -specs=${STAGING_INCDIR}/Xilinx.spec ${ESW_CFLAGS} "
LDFLAGS:append:xilinx-standalone = " -no-pie "
OECMAKE_C_LINK_FLAGS:append:xilinx-standalone = " --sysroot=${STAGING_DIR_HOST} -lxil -lxilstandalone -lxiltimer "

cmake_do_generate_toolchain_file:append:xilinx-standalone() {
    cat >> ${WORKDIR}/toolchain.cmake <<EOF
    set (CMAKE_SYSTEM_PROCESSOR "${LIBMETAL_CMAKE_SYSTEM_PROCESSOR}" )
    set (CROSS_PREFIX           "${LIBMETAL_CROSS_PREFIX}" CACHE STRING "")
    set (MACHINE "${LIBMETAL_MACHINE}" )
    set (PROJECT_VENDOR "xlnx")
    set (CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ")
    set (CMAKE_C_ARCHIVE_CREATE "<CMAKE_AR> qcs <TARGET> <LINK_FLAGS> <OBJECTS>")
    set (CMAKE_SYSTEM_NAME "${LIBMETAL_SYSTEM_NAME}" CACHE STRING "" FORCE)
    set (CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER CACHE STRING "")
    set (CMAKE_FIND_ROOT_PATH_MODE_LIBRARY NEVER CACHE STRING "")
    set (CMAKE_FIND_ROOT_PATH_MODE_INCLUDE NEVER CACHE STRING "")
    include (CMakeForceCompiler)
    CMAKE_FORCE_C_COMPILER("${OECMAKE_C_COMPILER}" GNU)
    set (CMAKE_LIBRARY_PATH "${PKG_CONFIG_SYSROOT_DIR}/usr/lib" CACHE STRING "")
    set (CMAKE_C_ARCHIVE_FINISH   true)
    set (CMAKE_INCLUDE_PATH "${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
    set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${PKG_CONFIG_SYSROOT_DIR}/usr/include/   " CACHE STRING "")
    include (cross-${LIBMETAL_SYSTEM_NAME}-gcc)
    set (WITH_DOC OFF)
    set_property (GLOBAL PROPERTY HAS_SYSTEM_DT ON)
EOF
}

do_install:append:xilinx-standalone() {
    install -d ${D}/${libdir}
    install -m 0755 ${B}/lib/libmetal.a ${D}/${libdir}
}
do_deploy() {
        install -d ${DEPLOYDIR}/
	install -Dm 0644 ${D}/${libdir}/*.a ${DEPLOYDIR}/
}
do_deploy:linux() {
}

addtask do_deploy after do_install
FILES:${PN}:xilinx-standalone = " ${libdir}/*.a "
