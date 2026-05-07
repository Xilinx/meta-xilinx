SUMMARY = "AMD Xilinx libmetal IRQ shared-memory demo for the remote \
core."
DESCRIPTION = "Builds the libmetal IRQ / shared-memory demo intended \
to run as the remote-side firmware on the RPU/MicroBlaze, paired with \
the host-side libmetal-demo-host application."
require ${LAYER_PATH_openamp-layer}/recipes-openamp/rpmsg-examples/rpmsg-example.inc
REPO = "git://github.com/Xilinx/openamp-system-reference.git;protocol=https"
SRCREV = "fab6139f6dd306383a4d4b666667993b91ac2bd9"
BRANCH="2026"

inherit ccmake cmake python3-dir deploy

SYSTEM_DTFILE_DEPENDS ??= ""
LIBMETAL_DEPENDS ?= "${SYSTEM_DTFILE_DEPENDS}"
LIBMETAL_DTFILE ?= "${CONFIG_DTFILE}"

DEPENDS:append = "${LIBMETAL_DEPENDS}"
DEPENDS:append = " libmetal-xlnx device-tree python3-native python3-pyyaml-native  python3-dtc-native libmetal-xlnx-extension xiltimer ttcps libxil xilstandalone xiltimer ipipsu "

PM_DEPENDS ?= " xilpm "
PM_DEPENDS:versal-2ve-2vm = " xilpm-ng "
DEPENDS:append = " ${PM_DEPENDS} "

B = "${WORKDIR}/build"
S = "${WORKDIR}/git/examples/libmetal"
OECMAKE_SOURCEPATH = "${S}"

OECMAKE_C_LINK_FLAGS:append = " --sysroot=${STAGING_DIR_HOST} -lxil -lxilstandalone -lxiltimer -lmetal "
CFLAGS:append = " -DSDT ${DEBUG_PREFIX_MAP} -specs=${PKG_CONFIG_SYSROOT_DIR}/usr/include/Xilinx.spec ${ESW_CFLAGS} "

LIBMETAL_CMAKE_SYSTEM_NAME = "unknown"
LIBMETAL_CMAKE_SYSTEM_NAME:xilinx-standalone = "Generic"

COMPATIBLE_HOST = ".*"
COMPATIBLE_HOST:armv7r = "[^-]*-[^-]*-eabi"
COMPATIBLE_HOST:armv8r = "[^-]*-[^-]*-eabi"

PM_LIB ?= "-D_xilpm_lib_path=${PKG_CONFIG_SYSROOT_DIR}/usr/lib/xilpm.a"
PM_LIB:versal-2ve-2vm = ""

EXTRA_OECMAKE:append:xilinx-standalone = " \
	-DCMAKE_LIBRARY_PATH=${PKG_CONFIG_SYSROOT_DIR}/usr/lib/ \
	-DPROJECT_MACHINE=amd_rpu \
	-DPROJECT_SYSTEM=generic \
	-DROLE=remote \
	-DDEMO=irq_shmem_demo \
        ${PM_LIB} \
	"

SOC ?= ""
SOC:versal-2ve-2vm = "VERSAL2"
SOC:versal-net = "VERSAL_NET"
SOC:versal = "VERSAL"
SOC:zynqmp = "ZYNQMP"
DEMO_CFG_FILE ?= "config_example.cmake"
LINKER_METADATA_FILE ?= "ExamplesExample.cmake"

cmake_do_generate_toolchain_file:append:arm() {
    cat >> ${WORKDIR}/toolchain.cmake <<EOF
	include (CMakeForceCompiler)
	CMAKE_FORCE_C_COMPILER("${OECMAKE_C_COMPILER}" GNU)
	set (CMAKE_SYSTEM_PROCESSOR "${TRANSLATED_TARGET_ARCH}" )
	set (CMAKE_SYSTEM_NAME      "${LIBMETAL_CMAKE_SYSTEM_NAME}")
	set (CMAKE_LIBRARY_PATH     "${CMAKE_LIBRARY_PATH}:${PKG_CONFIG_SYSROOT_DIR}/usr/lib" CACHE STRING "" FORCE)
	set (CMAKE_INCLUDE_PATH     "${CMAKE_INCLUDE_PATH} ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
	set (CMAKE_FIND_ROOT_PATH   "${CMAKE_FIND_ROOT_PATH} ${STAGING_LIBDIR} ${CMAKE_INCLUDE_PATH} " CACHE STRING "")

	set (LIBMETAL_INCLUDE_DIR   " ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
	set (LIBMETAL_LIB_DIR       " ${PKG_CONFIG_SYSROOT_DIR}/usr/lib" CACHE STRING "")

	set (XIL_INCLUDE_DIR        " ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
	set (CMAKE_C_FLAGS          " ${CMAKE_C_FLAGS}  ${PKG_CONFIG_SYSROOT_DIR}/usr/include/" CACHE STRING "")
        set_property(GLOBAL PROPERTY DEMO_CFG_FILE "${S}/${DEMO_CFG_FILE}")
        set_property(GLOBAL PROPERTY LINKER_METADATA_FILE ${S}/${LINKER_METADATA_FILE})
        set_property(GLOBAL PROPERTY SOC ${SOC})
EOF
}

FW_MACHINE:zynqmp = "cortexr5-1"
FW_MACHINE:versal = "cortexr5-1"
FW_MACHINE:versal-net = "cortexr52-1"
FW_MACHINE:versal-2ve-2vm = "cortexr52-1"
FW_OS ?= "${MCNAME}"
SUPPORTED_OS_LIST = "baremetal"
BBCLASSEXTEND = "${@' '.join(['mcextend:'+x for x in d.getVar('SUPPORTED_OS_LIST').split()])}"
python () {
    if not d.getVar("MCNAME"):
        raise bb.parse.SkipRecipe("No class extension set, %s is supported MCNAME: %s " % (d.getVar('SUPPORTED_OS_LIST'), d.getVar('MCNAME') ) ) 

}

FW_TARGET ?= "${MACHINE}-${FW_MACHINE}-${FW_OS}"

# no apps for linux build. this is handled by rpmsg-example recipes
do_configure:linux[noexec] = "1"
do_compile:linux[noexec] = "1"
do_configure[depends] += "  libxil:do_install xilstandalone:do_install  dtc-native:do_populate_sysroot "

addtask do_deploy before do_build after do_install

do_install() {
	install -d ${D}/${bindir}
	install -m 0755 ${B}/machine/remote/amd_rpu/irq_shmem_demo.elf ${D}/${bindir}/irq_shmem_demo.elf
}

do_deploy() {
	install -d ${DEPLOYDIR}/
	install -Dm 0644 ${D}/${bindir}/* ${DEPLOYDIR}/
}

do_configure[depends] += " lopper-native:do_install libxil:do_install xilstandalone:do_install lopper-native:do_populate_sysroot dtc-native:do_populate_sysroot "

do_configure:prepend() {
    export LOPPER_DTC_FLAGS="-b 0 -@"
    cd ${S}

    lopper ${LIBMETAL_DTFILE} -- openamp \
      --libmetal_output_file --compatible-string=libmetal,ipc-v1 \
      --processor=${ESW_MACHINE}  --os=baremetal_dt \
      --openamp_output_filename=${DEMO_CFG_FILE}

    if [ -n "${S}/config_example.cmake" ]; then
        if [ ! -e "${S}/config_example.cmake" ]; then
            bberror "${S}/config_example.cmake is not present, we can't continue"
            exit 1
        fi
    fi

    lopper ${LIBMETAL_DTFILE} -- baremetallinker_xlnx ${ESW_MACHINE} ${S} openamp

    if [ -n "${S}/${LINKER_METADATA_FILE}" ]; then
        if [ ! -e "${S}/${LINKER_METADATA_FILE}" ]; then
            bberror "${S}/${LINKER_METADATA_FILE} is not present, we can't continue"
            exit 1
        fi
    fi

    cd -
}

addtask do_deploy before do_build after do_install
FILES:${PN} = " ${bindir}/*.elf "

LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=ab88daf995c0bd0071c2e1e55f3d3505"
PV .= "+git"
PROVIDES += "libmetal-demo-remote"
