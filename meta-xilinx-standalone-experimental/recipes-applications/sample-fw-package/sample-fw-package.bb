SUMMARY = "Sample recipe to package and deploy baremetal or freertos elf or bin to linux rootfs"
LICENSE = "CLOSED"

inherit fw-package

FW_NAME = "hello-world"

TARGET_MC = "cortexr5-0-zynqmp-baremetal"

FW_MCDEPENDS := "${@bb.utils.contains('BBMULTICONFIG', '${TARGET_MC}', 'mc::${TARGET_MC}:${FW_NAME}:do_deploy', '', d)}"
FW_DEPLOY_DIR := "${@bb.utils.contains('BBMULTICONFIG', '${TARGET_MC}', '${TOPDIR}/tmp-${TARGET_MC}/deploy/images/${MACHINE}', '${DEPLOY_DIR_IMAGE}', d)}"
