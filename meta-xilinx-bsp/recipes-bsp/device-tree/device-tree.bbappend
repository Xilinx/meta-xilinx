FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# device tree sources for the various machines
COMPATIBLE_MACHINE:qemu-zynq7 = ".*"
SRC_URI:append:qemu-zynq7 = " file://qemu-zynq7.dts"

SRC_URI:append = "${@bb.utils.contains('MACHINE_FEATURES', 'provencore', ' file://pnc.dtsi', '', d)}"

do_configure:append() {
    if [ ${@bb.utils.contains('MACHINE_FEATURES', 'provencore', 'true', '', d)} ]; then
	echo '#include "pnc.dtsi"' >> ${DT_FILES_PATH}/system-top.dts
    fi
}
