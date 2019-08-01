inherit esw

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/"
ESW_COMPONENT_NAME = "libxil.a"

DEPENDS += "dtc-native python3-pyyaml-native xilmem"

# At this point A53 comes from DTG and MB from ESW, hence the different flow
# this needs to be fixed on DTG to unify the flow
DTBFILE_zynqmp-pmu ?= "${WORKDIR}/git/dts_files/output.dtb"
DTBFILE_cortexa53 ?= "${RECIPE_SYSROOT}/boot/devicetree/system-top.dtb"

do_generate_cmake (){
    # This will generate CMakeLists.txt which contains
    # drivers for the libxil 
    cd ${WORKDIR}/git
    DRIVERS_LIST=$(nativepython3 ${WORKDIR}/git/scripts/getdrvlist.py -d ${DTBFILE})
}
do_generate_driver_data (){
    # This script should also not rely on relative paths and such
    cd ${WORKDIR}/git
    nativepython3 ${WORKDIR}/git/scripts/generate_drvdata.py -d ${DTBFILE}
}

do_create_dtb(){
    :
}

do_create_dtb_append_zynqmp-pmu (){
    cd ${WORKDIR}
    ${BUILD_CPP} -nostdinc -I ${WORKDIR}/git/dts_files/include -undef -x assembler-with-cpp ${WORKDIR}/git/dts_files/zynqmp-zcu102-rev1.0.dts > ${WORKDIR}/git/test.dts
    dtc -I dts -O dtb -b 0 -@ -o ${DTBFILE} ${WORKDIR}/git/test.dts
}

addtask do_generate_cmake before do_configure after do_create_dtb

addtask do_generate_driver_data before do_compile after do_create_dtb

addtask do_create_dtb before do_compile after do_prepare_recipe_sysroot

DEPENDS_append_cortexa53 = " device-tree"


