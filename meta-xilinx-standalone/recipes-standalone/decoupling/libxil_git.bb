inherit esw

ESW_COMPONENT_SRC = "/XilinxProcessorIPLib/drivers/"
ESW_COMPONENT_NAME = "libxil.a"

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
