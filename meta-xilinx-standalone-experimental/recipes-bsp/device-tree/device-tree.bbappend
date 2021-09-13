CONFIG_DTFILE ?= ""

do_install_append_xilinx-standalone() {
    for DTB_FILE in ${CONFIG_DTFILE}; do
        install -Dm 0644 ${DTB_FILE} ${D}/boot/devicetree/$(basename ${DTB_FILE})
    done
}

do_deploy_append_xilinx-standalone() {
    for DTB_FILE in ${CONFIG_DTFILE}; do
        install -Dm 0644 ${DTB_FILE} ${DEPLOYDIR}/devicetree/$(basename ${DTB_FILE})
    done
}
