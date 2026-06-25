inherit ccmake cmake python3-dir deploy

SYSTEM_DTFILE_DEPENDS ??= ""
OPENAMP_DEPENDS ?= "${SYSTEM_DTFILE_DEPENDS}"
OPENAMP_DTFILE ?= "${SYSTEM_DTFILE}"

DEPENDS += "${OPENAMP_DEPENDS}"
DEPENDS += "open-amp-xlnx libmetal-xlnx python3-native python3-pyyaml-native lopper-native python3-dtc-native"

DEPENDS_BAREMETAL ??= ""
DEPENDS_BAREMETAL:armv7r = "xiltimer libxil xilstandalone xiltimer "
DEPENDS_BAREMETAL:armv8r = "xiltimer libxil xilstandalone xiltimer "
DEPENDS_BAREMETAL:linux = ""
DEPENDS += "${DEPENDS_BAREMETAL}"

DEPENDS_FREERTOS ??= ""
DEPENDS_FREERTOS:xilinx-freertos = "freertos10-xilinx"

DEPENDS += "${DEPENDS_FREERTOS}"

B = "${WORKDIR}/build"
S = "${WORKDIR}/git/examples/legacy_apps"
OECMAKE_SOURCEPATH = "${S}"

OECMAKE_C_LINK_FLAGS:append = " --sysroot=${STAGING_DIR_HOST} -lxil -lxilstandalone -lxiltimer -lopen_amp "
CFLAGS:append = " -DSDT -D_AMD_GENERATED_ ${DEBUG_PREFIX_MAP} "
CFLAGS:append:arm = " -O3  -DXLNX_PLATFORM \
 -specs=${PKG_CONFIG_SYSROOT_DIR}/usr/include/Xilinx.spec \
 "

CFLAGS:append = " ${ESW_CFLAGS} "

OPENAMP_CMAKE_SYSTEM_NAME = "unknown"
OPENAMP_CMAKE_SYSTEM_NAME:xilinx-standalone = "Generic"
OPENAMP_CMAKE_SYSTEM_NAME:xilinx-freertos = "FreeRTOS"
OPENAMP_CMAKE_SYSTEM_NAME:linux = "Linux"

PACKAGE_ARCH:linux = "${MACHINE_ARCH}"
COMPATIBLE_HOST = ".*"
COMPATIBLE_HOST:armv7r = "[^-]*-[^-]*-eabi"
COMPATIBLE_HOST:armv8r = "[^-]*-[^-]*-eabi"

OPENAMP_CMAKE_SOC = "unknown"
OPENAMP_CMAKE_SOC:zynqmp = "ZYNQMP"
OPENAMP_CMAKE_SOC:versal = "VERSAL"
OPENAMP_CMAKE_SOC:versal-net = "VERSAL_NET"

LINKER_METADATA_FILE="OpenAMP_Lopper_Linker_Cfg.cmake"

TOOLCHAIN_FILE_MACHINE:armv7r = "zynqmp_r5"
TOOLCHAIN_FILE_MACHINE:armv8r = "zynqmp_r5"
TOOLCHAIN_FILE_MACHINE:linux = "${OPENAMP_MACHINE}"

OPENAMP_MACHINE:xilinx-standalone = "${TOOLCHAIN_FILE_MACHINE}"
OPENAMP_MACHINE:xilinx-freertos = "${TOOLCHAIN_FILE_MACHINE}"

DEMO_LINK_FLAGS ?= ""
DEMO_LINK_FLAGS:append:armv7r = " -lxilstandalone -lscugic -lxiltimer"
DEMO_LINK_FLAGS:append:armv8r = " -lxilstandalone -lscugic -lxiltimer"
DEMO_LINK_FLAGS:append:xilinx-freertos = " -lfreertos"

OPENAMP_APP_NAME ?= "echo"

EXTRA_OECMAKE:append:xilinx-standalone = " \
	-DCMAKE_LIBRARY_PATH=${PKG_CONFIG_SYSROOT_DIR}/usr/lib/ \
	-DPROJECT_VENDOR=\"xlnx\"  \
	-DOPENAMP_APP_NAME=${OPENAMP_APP_NAME} -DMACHINE=${TOOLCHAIN_FILE_MACHINE} "

# Generate Lopper linker config file before generating toolchain file
cmake_do_generate_toolchain_file:append:arm() {
    cat >> ${WORKDIR}/toolchain.cmake <<EOF
	include (CMakeForceCompiler)
	CMAKE_FORCE_C_COMPILER("${OECMAKE_C_COMPILER}" GNU)
	set (CMAKE_SYSTEM_PROCESSOR "${TRANSLATED_TARGET_ARCH}" )
	set (CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ")
	set (CMAKE_SYSTEM_NAME      "${OPENAMP_CMAKE_SYSTEM_NAME}")
	set (MACHINE                "${TOOLCHAIN_FILE_MACHINE}" )
	set (CMAKE_LIBRARY_PATH     "${CMAKE_LIBRARY_PATH}:${PKG_CONFIG_SYSROOT_DIR}/usr/lib" CACHE STRING "" FORCE)
	set (CMAKE_INCLUDE_PATH     "${CMAKE_INCLUDE_PATH} ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
	set (CMAKE_FIND_ROOT_PATH   "${CMAKE_FIND_ROOT_PATH} ${STAGING_LIBDIR} ${CMAKE_INCLUDE_PATH} " CACHE STRING "")
	set (PROJECT_VENDOR         "xlnx" CACHE STRING "" FORCE)

	set (LIBMETAL_INCLUDE_DIR   " ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
	set (LIBMETAL_LIB_DIR       " ${PKG_CONFIG_SYSROOT_DIR}/usr/lib" CACHE STRING "")

	set (OPENAMP_INCLUDE_DIR    " ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
	set (OPENAMP_LIB_DIR        " ${PKG_CONFIG_SYSROOT_DIR}/usr/lib" CACHE STRING "")

	set (XIL_INCLUDE_DIR        " ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
	set (CMAKE_C_FLAGS          " ${CMAKE_C_FLAGS}  ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")

	set_property (GLOBAL PROPERTY HAS_SYSTEM_DT ON)
	set_property(GLOBAL PROPERTY SOC "${OPENAMP_CMAKE_SOC}")
	set_property(GLOBAL PROPERTY LINKER_METADATA_FILE ${S}/${LINKER_METADATA_FILE})
	set (DEMO_LINK_FLAGS       "${DEMO_LINK_FLAGS}" CACHE STRING "")
EOF
}

LOPPER_GEN_FILE_OUTPUT:armv7r = "zynqmp_r5"
LOPPER_GEN_FILE_OUTPUT:armv8r = "zynqmp_r5"

FW_MACHINE:zynqmp = "cortexr5-0"
FW_MACHINE:versal = "cortexr5-0"
FW_MACHINE:versal-net = "cortexr52-0"
FW_OS ?= "${MCNAME}"
SUPPORTED_OS_LIST = "baremetal freertos"
BBCLASSEXTEND = "${@' '.join(['mcextend:'+x for x in d.getVar('SUPPORTED_OS_LIST').split()])}"
python () {
    if not d.getVar("MCNAME"):
        raise bb.parse.SkipRecipe("No class extension set, %s is supported" % d.getVar('SUPPORTED_OS_LIST'))
}

FW_TARGET ?= "${MACHINE}-${FW_MACHINE}-${FW_OS}"

OPENAMP_CFG_HEADER = "${S}/machine/${LOPPER_GEN_FILE_OUTPUT}/amd_platform_info.h"
OPENAMP_CFG_HEADER:linux = ""

# no apps for linux build. this is handled by rpmsg-example recipes
do_configure:linux[noexec] = "1"
do_compile:linux[noexec] = "1"
do_configure[depends] += " lopper-native:do_install libxil:do_install xilstandalone:do_install lopper-native:do_populate_sysroot dtc-native:do_populate_sysroot "

addtask do_deploy before do_build after do_install

do_configure:prepend() {
    export LOPPER_DTC_FLAGS="-b 0 -@"

    cd ${S}/machine/${LOPPER_GEN_FILE_OUTPUT}
    lopper -O -f -v --enhanced  --permissive \
      -O . ${CONFIG_DTFILE} -- openamp --openamp_header_only \
      --openamp_output_filename=amd_platform_info.h \
      --openamp_remote=${ESW_MACHINE}
    cd -

    if [ -n "${OPENAMP_CFG_HEADER}" ]; then
        if [ ! -e "${OPENAMP_CFG_HEADER}" ]; then
           bberror "${OPENAMP_CFG_HEADER} is not present, we can't continue"
           exit 1
        fi
    fi

    cd ${S}/
    lopper -O ${S} ${CONFIG_DTFILE} -- baremetallinker_xlnx ${ESW_MACHINE} ${S} openamp
    mv *Example.cmake ${LINKER_METADATA_FILE}
    cd -

    sed -i -e 's/CXX//g' ${S}/cmake/vitis.cmake
    sed -i -e '/find_package(common)/d' ${S}/cmake/vitis.cmake
    touch ${S}/UserConfig.cmake
}

do_compile_rpu() {
        cmake_runcmake_build 
        ${OBJCOPY} -O binary  ${B}/osr_legacy_apps.elf ${B}/osr_legacy_apps.bin
}

do_install_rpu() {
	install -d ${D}/${bindir}
	install -m 0755 ${B}/osr_legacy_apps.elf ${D}/${bindir}/${PN}.elf
	install -m 0755 ${B}/osr_legacy_apps.bin ${D}/${bindir}/${PN}.bin
}

do_deploy() {
}
do_deploy_rpu() {
	install -d ${DEPLOYDIR}/
	install -Dm 0644 ${D}/${bindir}/* ${DEPLOYDIR}/
}

do_compile:armv7r() {
	do_compile_rpu
}
do_compile:armv8r() {
	do_compile_rpu
}

do_install:armv7r() {
	do_install_rpu
}
do_install:armv8r() {
	do_install_rpu
}

do_deploy:armv7r() {
	do_deploy_rpu
}
do_deploy:armv8r() {
	do_deploy_rpu
}

addtask do_deploy before do_build after do_install
FILES:${PN}:append:armv7r = " ${libdir}/*.a ${bindir}/*.elf ${bindir}/*bin ${bindir}/*out "
FILES:${PN}:append:armv8r = " ${libdir}/*.a ${bindir}/*.elf ${bindir}/*bin ${bindir}/*out "
