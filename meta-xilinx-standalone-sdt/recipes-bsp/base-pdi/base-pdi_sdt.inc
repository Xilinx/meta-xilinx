# Unset SRC_URI and use the PDI_PATH from PDI_PATH_DEPENDS (usually sdt-artifacts) SYSROOT_DIR

SRC_URI = ""

PDI_PATH_DEPENDS ??= ""
DEPENDS += "${PDI_PATH_DEPENDS}"

PDI_SKIP_CHECK = "1"

do_install() {
    if [ -f ${PDI_PATH} ]; then
        install -d ${D}/boot
        install -m 0644 ${PDI_PATH} ${D}/boot/base-design.pdi
    else
        bbfatal "No base pdi supplied"
    fi
}
