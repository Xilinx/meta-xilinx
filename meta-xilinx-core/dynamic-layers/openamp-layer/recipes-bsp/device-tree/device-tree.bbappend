FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://zynq-openamp.dtsi \
    file://zynqmp-openamp.dtsi \
    file://versal-openamp.dtsi \
"

# openamp.dtsi is in the WORKDIR
DT_INCLUDE:append = " ${WORKDIR}"

do_configure:append() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'openamp', ' true', 'false', d)} && [ "${ENABLE_OPENAMP_DTSI}" = "1" ]; then
		if [ -e "${DT_FILES_PATH}/system-top.dts" ]; then
			if [ -e "${WORKDIR}/${MACHINE}-openamp.dtsi" ]; then
				sed -i '/${MACHINE}-openamp\.dtsi/d' ${DT_FILES_PATH}/system-top.dts
				echo '/include/ "${MACHINE}-openamp.dtsi"' >> ${DT_FILES_PATH}/system-top.dts
			elif [ -e "${WORKDIR}/${SOC_FAMILY}-openamp.dtsi" ]; then
				sed -i '/${SOC_FAMILY}-openamp\.dtsi/d' ${DT_FILES_PATH}/system-top.dts
				echo '/include/ "${SOC_FAMILY}-openamp.dtsi"' >> ${DT_FILES_PATH}/system-top.dts
			else
				bbfatal "${MACHINE}-openamp.dtsi or ${SOC_FAMILY}-openamp.dtsi file is not available.  Cannot automatically add to system-top.dts."
			fi
		else
			bbfatal "system-top.dts not found in this configuration, cannot automatically add OpenAmp device tree nodes (openamp.dtsi)"
		fi
	fi
}
