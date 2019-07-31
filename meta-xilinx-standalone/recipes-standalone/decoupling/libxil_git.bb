inherit esw

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/"
ESW_COMPONENT_NAME = "libxil.a"

DEPENDS += "dtc-native python3-pyyaml-native xilmem"

DTBFILE ?= "${WORKDIR}/git/dts_files/output.dtb"

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

do_create_dtb (){
    # This is hard coded for MB for now
    if [[ "${DTBFILE}" == "${WORKDIR}/git/dts_files/output.dtb" ]]; then
        cd ${WORKDIR}
        # Which  dts are we supposed to use?
        # Should it include a dtsi?
        # Again this flow probably needs work
        # We should also use variables as input and output 
        ${BUILD_CPP} -nostdinc -I ${WORKDIR}/git/dts_files/include -undef -x assembler-with-cpp ${WORKDIR}/git/dts_files/zynqmp-zcu102-rev1.0.dts > ${WORKDIR}/git/test.dts
        dtc -I dts -O dtb -b 0 -@ -o ${DTBFILE} ${WORKDIR}/git/test.dts
    else
        # DTG flow
        :
    fi

}

addtask do_generate_cmake before do_configure after do_create_dtb

addtask do_generate_driver_data before do_compile after do_create_dtb

addtask do_create_dtb before do_compile after do_prepare_recipe_sysroot

DEPENDS_append_cortexa53 = " device-tree"

# Enable @ flag on dtc which is required by fsbl
YAML_ENABLE_DT_OVERLAY_cortexa53 = "1"
