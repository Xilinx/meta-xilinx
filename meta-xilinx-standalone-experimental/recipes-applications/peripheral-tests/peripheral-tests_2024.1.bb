inherit esw deploy

ESW_COMPONENT_SRC = "/lib/sw_apps/peripheral_tests/src/"

DEPENDS += "libxil xiltimer"

inherit python3native

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    cp -rf ${S}/scripts/linker_files/ ${S}/${ESW_COMPONENT_SRC}/linker_files
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}
    )
}

python do_generate_app_data() {
    import glob, subprocess, os

    system_dt = glob.glob(d.getVar('DTS_FILE'))
    srcdir = glob.glob(d.getVar('S'))
    src_dir = glob.glob(d.getVar('OECMAKE_SOURCEPATH'))
    machine = d.getVar('ESW_MACHINE')

    if len(system_dt) == 0:
        bb.error("Couldn't find device tree %s" % d.getVar('DTS_FILE'))

    if len(src_dir) == 0:
        bb.error("Couldn't find source dir %s" % d.getVar('OECMAKE_SOURCEPATH'))

    driver_name = d.getVar('REQUIRED_MACHINE_FEATURES')
    command = ["lopper"] + ["-f"] + ["-O"] + [src_dir[0]] + [system_dt[0]] + ["--"] + ["baremetal_gentestapp_xlnx"] + [machine] + [srcdir[0]]
    subprocess.run(command, check = True)
}
addtask do_generate_app_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"

PERIPHERAL_TEST_APP_IMAGE_NAME ??= "${BPN}"

inherit image-artifact-names

PERIPHERAL_TEST_BASE_NAME ?= "${PERIPHERAL_TEST_APP_IMAGE_NAME}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

ESW_COMPONENT ??= "peripheral_tests.elf"

do_compile:append() {
    ${OBJCOPY} -O binary ${B}/${ESW_COMPONENT} ${B}/peripheral_tests.bin
}

do_install() {
    :
}

do_deploy() {
    install -Dm 0644 ${B}/${ESW_COMPONENT} ${DEPLOYDIR}/${PERIPHERAL_TEST_BASE_NAME}.elf
    ln -sf ${PERIPHERAL_TEST_BASE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}.elf
    install -m 0644 ${B}/peripheral_tests.bin ${DEPLOYDIR}/${PERIPHERAL_TEST_BASE_NAME}.bin
    ln -sf ${PERIPHERAL_TEST_BASE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}.bin
}
addtask deploy before do_build after do_install
