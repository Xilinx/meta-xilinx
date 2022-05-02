FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = "${@bb.utils.contains('MACHINE_FEATURES', 'provencore', ' file://pnc.dtsi', '', d)}"

do_configure:append() {
    if [ ${@bb.utils.contains('MACHINE_FEATURES', 'provencore', 'true', '', d)} ]; then
	echo '#include "pnc.dtsi"' >> ${DT_FILES_PATH}/system-top.dts
    fi
}
