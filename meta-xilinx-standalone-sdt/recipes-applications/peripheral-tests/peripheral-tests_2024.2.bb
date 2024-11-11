inherit esw python3native esw_apps_common

ESW_COMPONENT_SRC = "/lib/sw_apps/peripheral_tests/src/"

DEPENDS += "libxil xiltimer"

ESW_EXECUTABLE_NAME = "peripheral_tests"

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

do_compile:append() {
    ${OBJCOPY} -O binary ${B}/${ESW_EXECUTABLE_NAME}.elf ${B}/${ESW_EXECUTABLE_NAME}.bin
}

do_install() {
    :
}

do_deploy() {
    install -Dm 0644 ${B}/${ESW_EXECUTABLE_NAME}.elf ${DEPLOYDIR}/${APP_IMAGE_NAME}.elf
    ln -sf ${APP_IMAGE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}-${BB_CURRENT_MC}.elf
    install -m 0644 ${B}/${ESW_EXECUTABLE_NAME}.bin ${DEPLOYDIR}/${APP_IMAGE_NAME}.bin
    ln -sf ${APP_IMAGE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}-${BB_CURRENT_MC}.bin
}
