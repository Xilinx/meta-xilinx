# Default fsbl configuration, using fsbl-fw multiconfig
#
# This requires MULTICONFIG += "versal-fw" to be added to local.conf
PSM_DEPENDS ?= ""
PSM_MCDEPENDS ?= "mc::versal-fw:psm-firmware:do_deploy"
PSM_FIRMWARE_IMAGE_NAME ?= "psm-firmware-${MACHINE}"
PSM_FIRMWARE_DEPLOY_DIR ?= "${TOPDIR}/tmp-microblaze-versal-fw/deploy/images/${MACHINE}"

PLM_DEPENDS ?= ""
PLM_MCDEPENDS ?= "mc::versal-fw:plm-firmware:do_deploy"
PLM_IMAGE_NAME ?= "plm-${MACHINE}"
PLM_DEPLOY_DIR ?= "${TOPDIR}/tmp-microblaze-versal-fw/deploy/images/${MACHINE}"

