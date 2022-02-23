# Default fsbl configuration, using fsbl-fw multiconfig
#
# This requires MULTICONFIG += "fsbl-fw" to be added to local.conf
FSBL_DEPENDS ?= ""
FSBL_MCDEPENDS ?= "mc::fsbl-fw:fsbl-firmware:do_deploy"
FSBL_DEPLOY_DIR ?= "${TOPDIR}/tmp-fsbl-fw/deploy/images/${MACHINE}"
FSBL_IMAGE_NAME ?= "fsbl-${MACHINE}"
