inherit check_sdt_enabled python3native xlnx-embeddedsw pkgconfig cmake

# Poky always tries to enable EXPORT_COMPILE_COMMANDS, but ESW changes
# behavior when this is enabled and will generate:
#    -isystem /usr/include
# which will cause a build failures.
OECMAKE_ARGS:remove = "-DCMAKE_EXPORT_COMPILE_COMMANDS:BOOL=ON"

SRCREV_FORMAT = "src_decouple"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"
OECMAKE_SOURCEPATH = "${S}/${ESW_COMPONENT_SRC}"
LICFILENAME = "license.txt"

SPECFILE_PATH:arm = "${S}/scripts/specs/arm/Xilinx.spec"
SPECFILE_PATH:aarch64 = "${S}/scripts/specs/arm/Xilinx.spec"
SPECFILE_PATH:microblaze = "${S}/scripts/specs/microblaze/Xilinx.spec"

ESW_MACHINE ?= "${MACHINE}"

ESW_CFLAGS += "-specs=${SPECFILE_PATH}"

inherit ccmake

COMPATIBLE_HOST = ".*-elf"
COMPATIBLE_HOST:arm = "[^-]*-[^-]*-eabi"

CONFIG_DTFILE ??= ""
DTS_FILE = "${DEPLOY_DIR_IMAGE}/devicetree/${@os.path.basename(d.getVar('CONFIG_DTFILE').replace('.dts','.dtb'))}"

DEPENDS += "python3-pyyaml-native lopper-native device-tree python3-dtc-native"

# We need the deployed output
do_configure[depends] += "device-tree:do_deploy"
do_compile[depends] += "device-tree:do_deploy"
do_install[depends] += "device-tree:do_deploy"

def get_xlnx_cmake_processor(tune, machine, d):
    cmake_processor = tune
    if tune.startswith('microblaze'):
        if (machine == 'psu_pmu_0'):
            cmake_processor = 'pmu_microblaze'
        elif (machine in [ 'psv_pmc_0', 'psx_pmc_0' ]):
            cmake_processor = 'plm_microblaze'
        else:
            cmake_processor = 'microblaze'
    elif (tune in [ 'cortexr5', 'cortexr5hf' ]):
        cmake_processor = 'cortexr5'
    elif (tune in [ 'cortexr52', 'cortexr52hf' ]):
        cmake_processor = 'cortexr52'
    elif tune.startswith('cortexa9'):
        cmake_processor = 'cortexa9'
    elif (tune in [ 'cortexa53', 'cortexa72-cortexa53' ]):
        cmake_processor = 'cortexa53'
    elif tune == 'cortexa72':
        cmake_processor = 'cortexa72'
    elif tune == 'cortexa78':
        cmake_processor = 'cortexa78'
    return cmake_processor

XLNX_CMAKE_MACHINE = "undefined"
XLNX_CMAKE_MACHINE:zynq = "Zynq"
XLNX_CMAKE_MACHINE:zynqmp = "ZynqMP"
XLNX_CMAKE_MACHINE:versal = "Versal"
XLNX_CMAKE_MACHINE:versal-net = "VersalNet"

XLNX_CMAKE_PROCESSOR = "${@get_xlnx_cmake_processor(d.getVar('DEFAULTTUNE'), d.getVar('ESW_MACHINE'), d)}"
XLNX_CMAKE_SYSTEM_NAME ?= "Generic"
XLNX_CMAKE_BSP_VARS ?= ""

cmake_do_generate_toolchain_file:append() {
    cat >> ${WORKDIR}/toolchain.cmake <<EOF
    include(CMakeForceCompiler)
    CMAKE_FORCE_C_COMPILER("${OECMAKE_C_COMPILER}" GNU)
    CMAKE_FORCE_CXX_COMPILER("${OECMAKE_CXX_COMPILER}" GNU)
    set( CMAKE_SYSTEM_PROCESSOR "${XLNX_CMAKE_PROCESSOR}" )
    set( CMAKE_MACHINE "${XLNX_CMAKE_MACHINE}" )
    # Will need this in the future to make cmake understand esw variables
    # set( CMAKE_SYSTEM_NAME `echo elf | sed -e 's/^./\u&/' -e 's/^\(Linux\).*/\1/'` )
    set( CMAKE_SYSTEM_NAME "${XLNX_CMAKE_SYSTEM_NAME}" )
    set( CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} ${S}/cmake)
    set( CMAKE_LIBRARY_PATH ${B})
    if ("${XLNX_CMAKE_PROCESSOR}" STREQUAL "plm_microblaze")
        set( CMAKE_BUILD_TYPE Release)
    endif()
    add_definitions( "${XLNX_CMAKE_BSP_VARS} -DSDT" )
EOF
}

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    install -m 0755  ${B}/${ESW_COMPONENT_NAME} ${D}${libdir}
    install -m 0644  ${B}/include/*.h ${D}${includedir}
}

CFLAGS:append = " ${ESW_CFLAGS}"
EXTRA_OECMAKE += "-DYOCTO=ON"

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx ${ESW_MACHINE} ${S}/lib/bsp/standalone/src/ hwcmake_metadata ${S}
    install -m 0755 StandaloneExample.cmake ${S}/cmake/Findcommonmeta.cmake
    )
}

# We need to find the license file, which vaires depending on the component
# recurse a maximum of x times, could be fancier but it gets complicated since
# we dont know for certain we are running devtool or just externalsrc
python(){
    import os.path
    if bb.data.inherits_class('externalsrc', d) and d.getVar('EXTERNALSRC'):
        externalsrc = d.getVar('EXTERNALSRC')
        lic_file = d.getVar('LIC_FILES_CHKSUM', False)
        licpath=externalsrc
        for i in range(5):
            if os.path.isfile(licpath + '/' + d.getVar('LICFILENAME',True)):
                lic_file = lic_file.replace('${S}',licpath)
                d.setVar('LIC_FILES_CHKSUM', lic_file)
                return
            licpath=os.path.dirname(licpath)
        bb.error("Couldn't find license file: %s, within directory %s or his parent directories" % (d.getVar('LICFILENAME',True), externalsrc))
}

do_generate_driver_data[dirs] = "${B}"
do_generate_driver_data[depends] += "device-tree:do_deploy"
python do_generate_driver_data() {
    import glob, subprocess, os

    system_dt = glob.glob(d.getVar('DTS_FILE'))
    src_dir = glob.glob(d.getVar('OECMAKE_SOURCEPATH'))
    machine = d.getVar('ESW_MACHINE')

    driver_name = d.getVar('REQUIRED_MACHINE_FEATURES')

    if len(system_dt) == 0:
        bb.error("Couldn't find device tree %s" % d.getVar('DTS_FILE'))

    if len(src_dir) == 0:
        bb.error("Couldn't find source dir %s" % d.getVar('OECMAKE_SOURCEPATH'))

    os.chdir(d.getVar('B'))
    command = ["lopper"] + ["-f"] + ["-O"] + [src_dir[0]] + [system_dt[0]] + ["--"] + ["baremetalconfig_xlnx.py"] + [machine] + [src_dir[0]]
    subprocess.run(command, check = True)
}
