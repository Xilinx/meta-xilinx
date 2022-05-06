FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# device tree sources for the various machines
COMPATIBLE_MACHINE:picozed-zynq7 = ".*"
SRC_URI:append:picozed-zynq7 = " file://picozed-zynq7.dts"

COMPATIBLE_MACHINE:zybo-linux-bd-zynq7 = ".*"
SRC_URI:append:zybo-linux-bd-zynq7 = " \
		file://zybo-linux-bd-zynq7.dts \
		file://pcw.dtsi \
		file://pl.dtsi \
		"

