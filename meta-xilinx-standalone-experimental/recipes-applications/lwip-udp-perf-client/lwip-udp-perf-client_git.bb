inherit esw deploy python3native

ESW_COMPONENT_SRC = "/lib/sw_apps/lwip_udp_perf_client/src/"

DEPENDS += "libxil lwip xiltimer"

do_configure:prepend() {
    (
    cd ${S}
    lopper ${DTS_FILE} -- baremetallinker_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC}
    install -m 0755 memory.ld ${S}/${ESW_COMPONENT_SRC}/
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
    )
}

do_generate_app_data() {
    # This script should also not rely on relative paths and such
    cd ${S}
    lopper ${DTS_FILE} -- bmcmake_metadata_xlnx.py ${ESW_MACHINE} ${S}/${ESW_COMPONENT_SRC} hwcmake_metadata ${S}
    install -m 0755 *.cmake ${S}/${ESW_COMPONENT_SRC}/
}
addtask do_generate_app_data before do_configure after do_prepare_recipe_sysroot
do_prepare_recipe_sysroot[rdeptask] = "do_unpack"

do_install() {
    install -d ${D}/${base_libdir}/firmware
    # Note that we have to make the ELF executable for it to be stripped
    install -m 0755  ${B}/lwip_udp_perf_client* ${D}/${base_libdir}/firmware
}

inherit image-artifact-names

LWIP_UDP_PERF_CLIENT_BASE_NAME ?= "${BPN}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

do_deploy() {

    # We need to deploy the stripped elf, hence why not doing it from ${D}
    install -Dm 0644 ${WORKDIR}/package/${base_libdir}/firmware/lwip_udp_perf_client.elf ${DEPLOYDIR}/${LWIP_UDP_PERF_CLIENT_BASE_NAME}.elf
    ln -sf ${LWIP_UDP_PERF_CLIENT_BASE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}.elf
    ${OBJCOPY} -O binary ${WORKDIR}/package/${base_libdir}/firmware/lwip_udp_perf_client.elf ${WORKDIR}/package/${base_libdir}/firmware/lwip_udp_perf_client.bin
    install -m 0644 ${WORKDIR}/package/${base_libdir}/firmware/lwip_udp_perf_client.bin ${DEPLOYDIR}/${LWIP_UDP_PERF_CLIENT_BASE_NAME}.bin
    ln -sf ${LWIP_UDP_PERF_CLIENT_BASE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}.bin
}

addtask deploy before do_build after do_package

FILES:${PN} = "${base_libdir}/firmware/lwip_udp_perf_client*"
