inherit esw python3native esw_apps_common deploy

DEPENDS += "libxil xiltimer bootgen-native"

RCONFLICTS:${PN} = "image-selector-xsct"

ESW_COMPONENT_SRC = "/src/"
ESW_EXECUTABLE_NAME = "imgsel"

SRC_URI:append = " git://github.com/Xilinx/image-selector.git;protocol=https;branch=main;destsuffix=image-selector;name=image-selector"
SRCREV_image-selector = "809441712855a64a35496192c180e31328a78b7b"

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}
    install -m 0644 *.cmake ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}/
    install -m 0644 ${S}/cmake/UserConfig.cmake ${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}
    )
}

OECMAKE_SOURCEPATH = "${WORKDIR}/${BPN}/${ESW_COMPONENT_SRC}"

do_compile:append () {
cat > ${WORKDIR}/${PN}.bif << EOF
    the_ROM_image:
    {
            [bootloader,destination_cpu=a53-0] ${B}/${ESW_EXECUTABLE_NAME}.elf
    }
EOF

    bootgen -image ${WORKDIR}/${PN}.bif -arch ${SOC_FAMILY} -w -o ${B}/${PN}.bin

    printf "* ${PN}\nSRCREV: ${SRCREV}\nBRANCH: ${BRANCH}\n\n" > ${S}/${PN}.manifest
}

do_install[noexec] = "1"

do_deploy() {
    install -Dm 0644 ${B}/${ESW_EXECUTABLE_NAME}.elf ${DEPLOYDIR}/${PN}.elf
    ln -sf ${PN}.elf ${DEPLOYDIR}/${PN}-${MACHINE}.elf
    install -Dm 0644 ${B}/${PN}.bin ${DEPLOYDIR}/${PN}.bin
    ln -sf ${PN}.bin ${DEPLOYDIR}/${PN}-${MACHINE}.bin

    install -Dm 0644 ${S}/${PN}.manifest ${DEPLOYDIR}/${PN}-${MACHINE}.manifest
}

addtask deploy before do_build after do_install
