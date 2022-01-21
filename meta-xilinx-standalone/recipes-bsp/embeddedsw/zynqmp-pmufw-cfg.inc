# Default fsbl configuration, using fsbl-fw multiconfig
#
# This requires MULTICONFIG += "zynqmp-pmufw" to be added to local.conf
PMU_DEPENDS ?= ""
PMU_MCDEPENDS ?= "mc::zynqmp-pmufw:pmu-firmware:do_deploy"
PMU_FIRMWARE_DEPLOY_DIR ?= "${TOPDIR}/tmp-microblaze-zynqmp-pmufw/deploy/images/${MACHINE}"
PMU_FIRMWARE_IMAGE_NAME ?= "pmu-firmware-${MACHINE}"
