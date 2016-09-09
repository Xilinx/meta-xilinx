inherit zynq7-platform-paths

do_configure_prepend() {
	if ${@bb.utils.contains('DEPENDS', 'virtual/zynq7-platform-init', 'true', 'false', d)}; then
		if [ -d "${S}/board/xilinx/zynq/custom_hw_platform" ]; then
			cp ${PLATFORM_INIT_STAGE_DIR}/ps7_init_gpl.h ${S}/board/xilinx/zynq/custom_hw_platform/
			cp ${PLATFORM_INIT_STAGE_DIR}/ps7_init_gpl.c ${S}/board/xilinx/zynq/custom_hw_platform/
		else
			cp ${PLATFORM_INIT_STAGE_DIR}/ps7_init_gpl.h ${S}/board/xilinx/zynq/
			cp ${PLATFORM_INIT_STAGE_DIR}/ps7_init_gpl.c ${S}/board/xilinx/zynq/
		fi
		if [ -n "${FORCE_PS7INIT}" ]; then
			# overwrite all the existing platforms ps7_init files, this is a shotgun approach and only works due to
			# u-boot being built for each machine seperately with seperate source directories.
			for i in ${S}/board/xilinx/zynq/*/; do
				cp ${PLATFORM_INIT_STAGE_DIR}/ps7_init_gpl.h $i
				cp ${PLATFORM_INIT_STAGE_DIR}/ps7_init_gpl.c $i
			done
		fi
	fi
}

FORCE_PS7INIT[doc] = "This variable is used to force the overriding of all ps7_init_gpl.* files in u-boot source with what is provided by virtual/zynq7-platform-init."

python () {
	# Determine if target machine needs to provide a custom ps7_init_gpl.*
	if d.getVar("SOC_FAMILY", True) == "zynq":
		if d.getVar("SPL_BINARY", True):
			# only add the dependency if u-bopt doesn't already provide the platform init files
			if d.getVar("FORCE_PS7INIT", True) or not bb.utils.contains("HAS_PS7INIT", d.getVar("UBOOT_MACHINE", True), True, False, d):
				# force the dependency on a recipe that provides the platform init files
				d.setVar("DEPENDS", "%s virtual/zynq7-platform-init" % d.getVar("DEPENDS", True))

		if d.getVar("SPL_BINARY", True) == "boot.bin":
			# Add this for backwards compatiblity
			d.setVar("PROVIDES", "%s virtual/boot-bin" % d.getVar("PROVIDES", True))
}

