inherit esw deploy features_check

ESW_COMPONENT_SRC = "XilinxProcessorIPLib/drivers/clockps/examples/"

REQUIRED_MACHINE_FEATURES = "clockps"

DEPENDS += "libxil xiltimer resetps"

inherit python3native

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    )
}

CLOCKPS_EX_IMAGE_NAME ??= "${BPN}"

inherit image-artifact-names

CLOCKPS_EX_NAME ?= "${CLOCKPS_EX_IMAGE_NAME}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

ESW_COMPONENT ??= "*.elf"

addtask deploy before do_build after do_install

do_install() {
    install -d ${D}/${base_libdir}/firmware
    install -m 0755  ${B}/*.elf ${D}/${base_libdir}/firmware
}

do_deploy() {
    install -d ${DEPLOYDIR}/${BPN}/
    install -Dm 0644 ${WORKDIR}/package/${base_libdir}/firmware/*.elf ${DEPLOYDIR}/${BPN}/
}
addtask deploy before do_build after do_package

FILES:${PN} = "${base_libdir}/firmware/*.elf"
