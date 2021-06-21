SUMMARY = "Xilinx BSP device trees"
DESCRIPTION = "Xilinx BSP device trees from within layer."
SECTION = "bsp"

# the device trees from within the layer are licensed as MIT, kernel includes are GPL
LICENSE = "MIT & GPLv2"
LIC_FILES_CHKSUM = " \
		file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
		file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6 \
		"

inherit devicetree

#this way of going through SRC_URI is better but if dts is including other dtsis, need to add all of them to SRC_URI..
#SRC_URI += "file://${SYSTEM_DTFILE}"
#DT_FILES_PATH = "${@d.getVar('WORKDIR')+'/'+os.path.dirname(d.getVar('SYSTEM_DTFILE'))}"

DT_FILES_PATH = "${@os.path.dirname(d.getVar('SYSTEM_DTFILE')) if d.getVar('SYSTEM_DTFILE') else d.getVar('S')}"

COMPATIBLE_MACHINE_zynqmp = ".*"
COMPATIBLE_MACHINE_versal = ".*"

# Device-trees are inherently board specific
BOARD_ARCH ??= "${MACHINE_ARCH}"
PACKAGE_ARCH = "${BOARD_ARCH}"

DEPENDS += "python3-dtc-native"

PROVIDES = "virtual/dtb"

# common zynq include
SRC_URI_append_zynq = " file://zynq-7000-qspi-dummy.dtsi"

# device tree sources for the various machines
COMPATIBLE_MACHINE_picozed-zynq7 = ".*"
SRC_URI_append_picozed-zynq7 = " file://picozed-zynq7.dts"

COMPATIBLE_MACHINE_qemu-zynq7 = ".*"
SRC_URI_append_qemu-zynq7 = " file://qemu-zynq7.dts"

COMPATIBLE_MACHINE_zybo-linux-bd-zynq7 = ".*"
SRC_URI_append_zybo-linux-bd-zynq7 = " \
		file://zybo-linux-bd-zynq7.dts \
		file://pcw.dtsi \
		file://pl.dtsi \
		"

COMPATIBLE_MACHINE_kc705-microblazeel = ".*"
SRC_URI_append_kc705-microblazeel = " \
		file://kc705-microblazeel.dts \
		file://pl.dtsi \
		file://system-conf.dtsi \
		"

