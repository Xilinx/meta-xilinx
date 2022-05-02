FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# device tree sources for the various machines
COMPATIBLE_MACHINE:picozed-zynq7 = ".*"
SRC_URI:append:picozed-zynq7 = " file://picozed-zynq7.dts"

COMPATIBLE_MACHINE:qemu-zynq7 = ".*"
SRC_URI:append:qemu-zynq7 = " file://qemu-zynq7.dts"

COMPATIBLE_MACHINE:zybo-linux-bd-zynq7 = ".*"
SRC_URI:append:zybo-linux-bd-zynq7 = " \
		file://zybo-linux-bd-zynq7.dts \
		file://pcw.dtsi \
		file://pl.dtsi \
		"

COMPATIBLE_MACHINE:kc705-microblazeel = ".*"
SRC_URI:append:kc705-microblazeel = " \
		file://kc705-microblazeel.dts \
		file://pl.dtsi \
		file://system-conf.dtsi \
		"
SRC_URI:append = "${@bb.utils.contains('MACHINE_FEATURES', 'provencore', ' file://pnc.dtsi', '', d)}"

do_configure:append() {
    if [ ${@bb.utils.contains('MACHINE_FEATURES', 'provencore', 'true', '', d)} ]; then
	echo '#include "pnc.dtsi"' >> ${DT_FILES_PATH}/system-top.dts
    fi
}
