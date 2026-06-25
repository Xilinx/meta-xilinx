inherit cmake ccmake deploy

S:xilinx-standalone = "${WORKDIR}/git"
B:xilinx-standalone = "${WORKDIR}/build"

OECMAKE_SOURCEPATH:xilinx-standalone = "${S}/"
PROVIDES:xilinx-standalone = "libmetal"

S:xilinx-freertos = "${WORKDIR}/git"
B:xilinx-freertos = "${WORKDIR}/build"

OECMAKE_SOURCEPATH:xilinx-freertos = "${S}/"
PROVIDES:xilinx-freertos = "libmetal libmetal-xlnx"


RPU_DEPENDS = " doxygen-native xilstandalone scugic xiltimer  nativesdk-xilinx-lops "
DEPENDS:armv7r:append = " ${RPU_DEPENDS} xilpm "
DEPENDS:armv8r:append = " ${RPU_DEPENDS} xilpm-ng "
DEPENDS:append:xilinx-freertos = " freertos10-xilinx "

DEPENDS:remove:microblaze = " scugic xiltimer "
DEPENDS:remove:armv7r = " sysfsutils eudev "

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

LIBMETAL_CMAKE_SYSTEM_PROCESSOR:xilinx-standalone = "${TRANSLATED_TARGET_ARCH}"
LIBMETAL_CMAKE_SYSTEM_PROCESSOR:xilinx-freertos = "${TRANSLATED_TARGET_ARCH}"

LIBMETAL_CMAKE_SYSTEM_PROCESSOR:xilinx-standalone:microblaze = "microblaze"

LIBMETAL_DEMO ?= "OFF"

EXTRA_OECMAKE:armv7r = "\
    -DWITH_EXAMPLES=${LIBMETAL_DEMO} \
    "

CFLAGS:append = " -DSDT -DXLNX_PLATFORM "

FILES:${PN}:xilinx-standalone = " ${libdir}/*.a ${bindir}/*elf ${bindir}/*bin "

cmake_do_generate_toolchain_file:append:armv7r() {
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

do_compile:append:armv7r() {
    ${OBJCOPY} -O binary ${B}/examples/system/${LIBMETAL_SYSTEM_NAME}/${LIBMETAL_MACHINE}/amp_demo/metal.elf  \
    ${B}/examples/system/${LIBMETAL_SYSTEM_NAME}/${LIBMETAL_MACHINE}/amp_demo/libmetal-demo.bin
}
do_install:append:armv7r() {
    install -d ${D}/${bindir}
    install -d ${D}/${libdir}

    install -m 0755 ${B}/lib/libmetal.a ${D}/${libdir}
    install -m 0755 ${B}/examples/system/${LIBMETAL_SYSTEM_NAME}/${LIBMETAL_MACHINE}/amp_demo/metal.elf ${D}/${bindir}/
    install -m 0755 ${B}/examples/system/${LIBMETAL_SYSTEM_NAME}/${LIBMETAL_MACHINE}/amp_demo/libmetal-demo.bin ${D}/${bindir}/
}
do_deploy() {
        install -d ${DEPLOYDIR}/
        install -Dm 0644 ${D}/${bindir}/*.elf ${DEPLOYDIR}/
	install -Dm 0644 ${D}/${bindir}/*.bin ${DEPLOYDIR}/
	install -Dm 0644 ${D}/${libdir}/*.a ${DEPLOYDIR}/
}
do_deploy:linux() {
}

addtask do_deploy after do_install
FILES:${PN}:append:armv7r = " ${libdir}/*.a ${bindir}/*.elf ${bindir}/*bin "
FILES:${PN}:append:armv8r = " ${libdir}/*.a ${bindir}/*.elf ${bindir}/*bin "
