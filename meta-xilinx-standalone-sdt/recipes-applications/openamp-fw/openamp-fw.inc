LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
LICENSE = "MIT"
INSANE_SKIP:${PN} = "arch"

FW_LIB_DIR = "${nonarch_base_libdir}/firmware"
INSANE_SKIP:${PN} = "arch"

OPENAMP_FW_SRC_NAME ?= "rpmsg-echo.out"
OPENAMP_FW_NAME ?= "image_echo_test"

FW_MACHINE:zynqmp = "cortexr5-0"
FW_MACHINE:versal = "cortexr5-0"
FW_MACHINE:versal-net = "cortexr52-0"
FW_OS ?= "baremetal"

FW_TARGET ?= "${MACHINE}-${FW_MACHINE}-${FW_OS}"

OPENAMP_FW_DEPLOY_DIR ?= "tmp-${FW_TARGET}/deploy"

DEPENDS = " open-amp-xlnx "
do_install[mcdepends] += "mc::${FW_TARGET}:open-amp-xlnx:do_deploy"
do_install() {
	install -d ${D}${FW_LIB_DIR}
	src=${TOPDIR}/${OPENAMP_FW_DEPLOY_DIR}/${OPENAMP_FW_SRC_NAME}
	dest=${D}${FW_LIB_DIR}/${OPENAMP_FW_NAME}
	install -m 0644 ${src} ${dest}
}

do_compile[noexec] = "1"

FILES:${PN} = "${FW_LIB_DIR}/${OPENAMP_FW_NAME}"
