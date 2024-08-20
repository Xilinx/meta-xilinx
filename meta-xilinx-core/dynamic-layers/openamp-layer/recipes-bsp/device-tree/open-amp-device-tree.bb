SUMMARY = "OpenAMP Device Tree Overlay for Xilinx devices."
SECTION = "bsp"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://zynqmp-openamp.dtsi \
    file://zynqmp-openamp-overlay.dts \
    file://versal-openamp.dtsi \
    file://versal-openamp-overlay.dts \
    file://versal-net-openamp.dtsi \
    file://versal-net-openamp-overlay.dts \
"

# We don't have anything to include from the kernel
KERNEL_INCLUDE = ""

COMPATIBLE_MACHINE:zynqmp = "${MACHINE}"
COMPATIBLE_MACHINE:versal = "${MACHINE}"
COMPATIBLE_MACHINE:versal-net = "${MACHINE}"

inherit devicetree image-artifact-names features_check

REQUIRED_DISTRO_FEATURES = "openamp"

# We are not _THE_ virtual/dtb provider
PROVIDES:remove = "virtual/dtb"

DEPENDS += "python3-dtc-native"

S = "${WORKDIR}/source"

# Set a default so something resolves
SOC_FAMILY ??= "SOC_FAMILY"

do_configure:prepend() {
	mkdir -p source

	if [ -e ${WORKDIR}/${MACHINE}-openamp-overlay.dts ]; then
		install ${WORKDIR}/${MACHINE}-openamp.dtsi ${WORKDIR}/source/. || :
		install ${WORKDIR}/${MACHINE}-openamp-overlay.dts ${WORKDIR}/source/openamp.dts
	elif [ -e ${WORKDIR}/${SOC_FAMILY}-openamp-overlay.dts ]; then
		install ${WORKDIR}/${SOC_FAMILY}-openamp.dtsi ${WORKDIR}/source/. || :
		install ${WORKDIR}/${SOC_FAMILY}-openamp-overlay.dts ${WORKDIR}/source/openamp.dts
	else
		bbfatal "${MACHINE}-openamp-overlay.dts or ${SOC_FAMILY}-openamp-overlay.dts file is not available.  Cannot automatically add OpenAMP dtbo file."
	fi
}

FILES:${PN} = "/boot/devicetree/openamp.dtbo"
