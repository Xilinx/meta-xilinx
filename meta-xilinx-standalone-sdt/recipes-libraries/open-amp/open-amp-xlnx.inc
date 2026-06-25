PROVIDES = " open-amp open-amp-xlnx "
RPROVIDES:${PN} += "open-amp"

inherit ccmake cmake python3-dir deploy

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

OECMAKE_SOURCEPATH = "${S}/"

DEPENDS += "libmetal "

OECMAKE_C_LINK_FLAGS:append:arm = " --sysroot=${STAGING_DIR_HOST}"
CFLAGS:append = " -DSDT -D_AMD_GENERATED_ -DVIRTIO_USE_DCACHE ${DEBUG_PREFIX_MAP} "
CFLAGS:append:armv7r = " -O3  -DXLNX_PLATFORM  -specs=${PKG_CONFIG_SYSROOT_DIR}/usr/include/Xilinx.spec "
CFLAGS:append:armv8r = " -O3  -DXLNX_PLATFORM  -specs=${PKG_CONFIG_SYSROOT_DIR}/usr/include/Xilinx.spec "
CFLAGS:append:xilinx-freertos = " -DUSE_FREERTOS "

OPENAMP_CMAKE_SYSTEM_NAME = "unknown"
OPENAMP_CMAKE_SYSTEM_NAME:xilinx-standalone = "Generic"
OPENAMP_CMAKE_SYSTEM_NAME:xilinx-freertos = "FreeRTOS"

TOOLCHAIN_FILE_MACHINE:armv7r = "zynqmp_r5"
TOOLCHAIN_FILE_MACHINE:armv8r = "zynqmp_r5"
TOOLCHAIN_FILE_MACHINE:linux = "${OPENAMP_MACHINE}"

OPENAMP_MACHINE:xilinx-standalone = "${TOOLCHAIN_FILE_MACHINE}"
OPENAMP_MACHINE:xilinx-freertos = "${TOOLCHAIN_FILE_MACHINE}"

# Generate Lopper linker config file before generating toolchain file
cmake_do_generate_toolchain_file:append:arm() {
    cat >> ${WORKDIR}/toolchain.cmake <<EOF
	include (CMakeForceCompiler)
	CMAKE_FORCE_C_COMPILER("${OECMAKE_C_COMPILER}" GNU)
	set (CMAKE_SYSTEM_PROCESSOR "${TRANSLATED_TARGET_ARCH}" )
	set (CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ")
	set (CMAKE_SYSTEM_NAME      "${OPENAMP_CMAKE_SYSTEM_NAME}")
	set (MACHINE                "${TOOLCHAIN_FILE_MACHINE}" )
	set (CMAKE_LIBRARY_PATH     "${PKG_CONFIG_SYSROOT_DIR}/usr/lib" CACHE STRING "")
	set (CMAKE_INCLUDE_PATH     "${CMAKE_INCLUDE_PATH} ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
	set (CMAKE_FIND_ROOT_PATH   "${CMAKE_FIND_ROOT_PATH} ${STAGING_LIBDIR} ${CMAKE_INCLUDE_PATH} " CACHE STRING "")
	set (LIBMETAL_INCLUDE_DIR   " ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
	set (LIBMETAL_LIB_DIR       " ${PKG_CONFIG_SYSROOT_DIR}/usr/lib" CACHE STRING "")
	set (XIL_INCLUDE_DIR        " ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
	set (CMAKE_C_FLAGS          " ${CMAKE_C_FLAGS}  ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
	set (WITH_PROXY             ON)
	set (WITH_APPS              OFF)
EOF
}

PACKAGE_ARCH:linux = "${MACHINE_ARCH}"
COMPATIBLE_HOST:linux = ".*"
COMPATIBLE_HOST:arm = "[^-]*-[^-]*-eabi"
