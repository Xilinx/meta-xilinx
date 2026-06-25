SUMMARY = "libmetal IRQ/shared-memory demo RPU firmware payload."
DESCRIPTION = "Pre-built RPU baremetal demo payload that exercises \
libmetal IRQ and shared-memory primitives between the APU and RPU."
require openamp-fw.inc

FW_OS = "baremetal"

TARGET_MC:zynqmp = "cortexr5-1-${FW_OS}"
TARGET_MC:versal = "cortexr5-1-${FW_OS}"
TARGET_MC:versal-net = "cortexr52-1-${FW_OS}"
TARGET_MC:versal-2ve-2vm = "cortexr52-1-${FW_OS}"

FW_MCDEPENDS = "mc::${MACHINE}-${TARGET_MC}:libmetal-demo-remote:do_deploy"

OPENAMP_FW_NAME = "irq_shmem_demo.elf"
OPENAMP_FW_SRC_NAME = "${OPENAMP_FW_NAME}"
OPENAMP_XLNX_RECIPE = "${OPENAMP_FW_NAME}"
FW_FILE = "${FW_DEPLOY_DIR}/${OPENAMP_FW_NAME}"
PROVIDES:append = " libmetal-fw-remote "
RPROVIDES:${PN} += "libmetal-fw-remote "

DESTDIR = "/boot"
SYSROOT_DIRS += "/boot"

do_deploy:append() {
    install -Dm 0644 ${FW_DEPLOY_DIR}/${OPENAMP_FW_SRC_NAME} ${DEPLOYDIR}/${OPENAMP_FW_NAME}
}

DEPENDS:remove = "open-amp-xlnx"

FILES:${PN} = "/boot/${OPENAMP_FW_NAME}"
