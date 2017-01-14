inherit xilinx-platform-init

PLATFORM_BOARD_DIR ?= ""
PLATFORM_BOARD_DIR_zynq = "board/xilinx/zynq"
PLATFORM_BOARD_DIR_zynqmp = "board/xilinx/zynqmp"

do_configure_prepend() {
	if ${@bb.utils.contains('DEPENDS', 'virtual/xilinx-platform-init', 'true', 'false', d)}; then
		for f in ${PLATFORM_INIT_FILES}; do
			if [ -d "${S}/${PLATFORM_BOARD_DIR}/custom_hw_platform" ]; then
				cp ${PLATFORM_INIT_STAGE_DIR}/$f ${S}/${PLATFORM_BOARD_DIR}/custom_hw_platform/
			else
				cp ${PLATFORM_INIT_STAGE_DIR}/$f ${S}/${PLATFORM_BOARD_DIR}/
			fi
			# Newer u-boot sources use the init files in a sub directory named
			# based on the name of the device tree. This is not straight
			# forward to detect. Instead of detecting just overwrite all the
			# platform init files so that the correct one is always used. This
			# shotgun approach only works due to this recipe being machine arch
			# specific. Do this overwrite un-conditionally as there is no
			# guarantees that the chosen board config does not have the device
			# tree config set.
			for i in ${S}/${PLATFORM_BOARD_DIR}/*/; do
				[ -d $i ] && cp ${PLATFORM_INIT_STAGE_DIR}/$f $i
			done
		done
	fi
}

FORCE_PLATFORM_INIT[doc] = "This variable is used to force the overriding of all platform init files in u-boot source."

python () {
	hasconfigs = (d.getVar("HAS_PLATFORM_INIT") or "").split() + (d.getVar("HAS_PS7INIT") or "").split()
	forceoverride = (d.getVar("FORCE_PLATFORM_INIT") == "1") or (d.getVar("FORCE_PS7INIT"))

	# Determine if target machine needs to provide a custom platform init files
	if d.getVar("SOC_FAMILY") in ["zynq", "zynqmp"]:
		if d.getVar("SPL_BINARY"):
			# only add the dependency if u-boot doesn't already provide the platform init files
			if forceoverride or not (d.getVar("UBOOT_MACHINE") in hasconfigs):
				# force the dependency on a recipe that provides the platform init files
				d.appendVar("DEPENDS", " virtual/xilinx-platform-init")

		if d.getVar("SPL_BINARY") == "boot.bin":
			# Add this for backwards compatibility
			d.setVar("PROVIDES", "%s virtual/boot-bin" % d.getVar("PROVIDES"))
}

