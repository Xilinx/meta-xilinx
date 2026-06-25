SUMMARY = "OpenAMP Zephyr demo RPU firmware payload."
DESCRIPTION = "Pre-built RPU Zephyr OpenAMP demo payload loaded by the \
APU-side OpenAMP example application on supported boards."
require openamp-fw.inc
MCNAME = "zephyr"

FW_OS ?= "${MCNAME}"

TARGET_MC:zynqmp = "cortexr5-0-${FW_OS}"
TARGET_MC:versal = "cortexr5-0-${FW_OS}"
TARGET_MC:versal-net = "cortexr52-0-${FW_OS}"
TARGET_MC:versal-2ve-2vm = "cortexr52-0-${FW_OS}"

FW_MC_MACHINE ?= ""
FW_MC_MACHINE:versal-2ve-2vm = "${MACHINE}-${TARGET_MC}"
FW_MC_MACHINE:versal-net = "${MACHINE}-${TARGET_MC}"

FW_MCDEPENDS = "mc::${FW_MC_MACHINE}:${OPENAMP_XLNX_RECIPE}-${FW_OS}:do_deploy"
FW_DEPLOY_DIR ?= "${TMPDIR}-${FW_MC_MACHINE}/deploy/images/${MACHINE}"
FW_MCDEPENDS = "mc::${FW_MC_MACHINE}:zephyr-openamp-rpmsg-multi-srv:do_deploy"

TARGET_MC = "cortexr52-0-zephyr"
OPENAMP_FW_NAME = "zephyr-openamp-rpmsg-multi-srv.elf"
OPENAMP_FW_SRC_NAME = "${OPENAMP_FW_NAME}"
OPENAMP_XLNX_RECIPE = "${OPENAMP_FW_NAME}"

PROVIDES:append:armv7r = " openamp-zephyr-demo "
PROVIDES:append:armv8r = " openamp-zephyr-demo "

RPROVIDES:${PN} += "openamp-zephyr-demo"
DEPENDS = ""
