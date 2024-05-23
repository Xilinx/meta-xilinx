LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
LICENSE = "MIT"
INSANE_SKIP:${PN} = "arch"

FW_LIB_DIR = "/lib/firmware"

OPENAMP_FW_SRC_NAME ?= "rpmsg-echo.out"
OPENAMP_FW_NAME ?= "image_echo_test"

FW_MACHINE:zynqmp = "cortexr5-0-zynqmp"
FW_MACHINE:versal = "cortexr5-0-versal"
FW_MACHINE:versal-net = "cortexr52-0-versal-net"
FW_OS ?= "baremetal"

OPENAMP_FW_DEPLOY_DIR ?= "${MC_TMPDIR_PREFIX}-${FW_MACHINE}-${FW_OS}/deploy/images/${MACHINE}"
DEPENDS = " open-amp-xlnx "
do_install[mcdepends] += "mc::${FW_MACHINE}-${FW_OS}:open-amp-xlnx:do_deploy"
do_install() {
	install -d ${D}${FW_LIB_DIR}
	src=${OPENAMP_FW_DEPLOY_DIR}/${OPENAMP_FW_SRC_NAME}
	dest=${D}${FW_LIB_DIR}/${OPENAMP_FW_NAME}
	install -m 0644 ${src} ${dest}
}

do_compile[noexec] = "1"

FILES:${PN} = "/lib/firmware/${OPENAMP_FW_NAME}"
