SUMMARY = "Sample baremetal firmware package recipe (template)."
DESCRIPTION = "Template recipe demonstrating how to package a custom \
baremetal firmware ELF as part of a multiconfig BSP build."
LICENSE = "CLOSED"

inherit fw-package

FW_NAME = "hello-world"

TARGET_MC = "cortexr5-0-zynqmp-baremetal"

FW_MCDEPENDS := "${@bb.utils.contains('BBMULTICONFIG', '${TARGET_MC}', 'mc::${TARGET_MC}:${FW_NAME}:do_deploy', '', d)}"
FW_DEPLOY_DIR := "${@bb.utils.contains('BBMULTICONFIG', '${TARGET_MC}', '${TOPDIR}/tmp-${TARGET_MC}/deploy/images/${MACHINE}', '${DEPLOY_DIR_IMAGE}', d)}"
