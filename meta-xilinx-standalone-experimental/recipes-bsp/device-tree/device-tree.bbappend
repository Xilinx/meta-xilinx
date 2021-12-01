CONFIG_DTFILE ?= ""

do_install:prepend() {
    for DTB_FILE in ${CONFIG_DTFILE}; do
        install -Dm 0644 ${DTB_FILE} ${D}/boot/devicetree/$(basename ${DTB_FILE})
    done
}

# This needs to run BEFORE the device tree version, otherwise it can error with
# Expected filename ${DTB_FILE_NAME} doesn't exist in ${DEPLOYDIR}/devicetree
devicetree_do_deploy:prepend() {
    for DTB_FILE in ${CONFIG_DTFILE}; do
        install -Dm 0644 ${DTB_FILE} ${DEPLOYDIR}/devicetree/$(basename ${DTB_FILE})
    done
}
