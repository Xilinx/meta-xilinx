require u-boot-xlnx.inc

COMPATIBLE_MACHINE = "zynq"

PROVIDES = "virtual/boot-bin"

SRCREV = "03464615e241054a38cd920980d6b12feba95585"
PV = "v2015.01${XILINX_EXTENSION}+git${SRCPV}"

SPL_BINARY = "boot.bin"
UBOOT_MAKE_TARGET ?= "boot.bin"

inherit zynq7-platform-paths

DEPENDS += "virtual/zynq7-platform-init"

do_configure_prepend() {
	[ -e ${PLATFORM_INIT_STAGE_DIR}/ps7_init_gpl.h ] && \
		cp ${PLATFORM_INIT_STAGE_DIR}/ps7_init_gpl.h ${S}/board/xilinx/zynq/
	[ -e ${PLATFORM_INIT_STAGE_DIR}/ps7_init_gpl.c ] && \
		cp ${PLATFORM_INIT_STAGE_DIR}/ps7_init_gpl.c ${S}/board/xilinx/zynq/
}

do_install () {
	if [ "x${SPL_BINARY}" != "x" ]; then
		install -d ${D}/boot
		install ${S}/${SPL_BINARY} ${D}/boot/${SPL_IMAGE}
		ln -sf ${SPL_IMAGE} ${D}/boot/${SPL_BINARY}
	fi
}

do_deploy () {
	if [ "x${SPL_BINARY}" != "x" ]; then
		install ${S}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_IMAGE}
		rm -f ${DEPLOYDIR}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_SYMLINK}
		ln -sf ${SPL_IMAGE} ${DEPLOYDIR}/${SPL_BINARY}
		ln -sf ${SPL_IMAGE} ${DEPLOYDIR}/${SPL_SYMLINK}
	fi
}
