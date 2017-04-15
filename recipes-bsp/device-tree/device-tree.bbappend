
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# common zynq include
SRC_URI_append_zynq = " file://zynq-7000-qspi-dummy.dtsi"

# device tree sources for the various machines
SRC_URI_append_picozed-zynq7 = " file://picozed-zynq7.dts"
SRC_URI_append_microzed-zynq7 = " file://microzed-zynq7.dts"
SRC_URI_append_qemu-zynq7 = " file://qemu-zynq7.dts"
SRC_URI_append_zybo-linux-bd-zynq7 = " \
		file://zybo-linux-bd-zynq7.dts \
		file://pcw.dtsi \
		file://pl.dtsi \
		"
SRC_URI_append_kc705-microblazeel = " \
		file://kc705-microblazeel.dts \
		file://pl.dtsi \
		file://system-conf.dtsi \
		"

