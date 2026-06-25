# Default RPU1 configuration, using the machine-specific baremetal multiconfig
#
# This requires BBMULTICONFIG += "${MACHINE}-cortex${RPU1_PROC}-${RPU1_OS}"
# to be added to local.conf
RPU1_DEPENDS ?= ""
RPU1_MCDEPENDS ?= "mc::${RPU1_MC}:hello-world:do_deploy"
RPU1_DEPLOY_DIR ?= "${TOPDIR}/tmp-${RPU1_MC}/deploy/images/${MACHINE}"
RPU1_IMAGE_NAME ?= "hello-world-${MACHINE}-${RPU1_MC}"
