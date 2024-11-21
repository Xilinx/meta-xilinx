# QEMU for the Kria SOM requires a section from the FSBL to be extracted

PMU_CONF_NAME ?= "pmu-conf"
PMU_CONF_BASE_NAME ?= "${PMU_CONF_NAME}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

# Required so we can run objcopy in do_compile
DEPENDS:append:zynqmp = " virtual/${TARGET_PREFIX}binutils"

do_compile:append:zynqmp () {
    ${OBJCOPY} --dump-section .sys_cfg_data=${B}/${PMU_CONF_NAME}.bin ${B}/${ESW_COMPONENT}
}

do_deploy:append:zynqmp () {
    install -Dm 0644 ${B}/${PMU_CONF_NAME}.bin ${DEPLOYDIR}/${PMU_CONF_BASE_NAME}.bin
    ln -s ${PMU_CONF_BASE_NAME}.bin ${DEPLOYDIR}/${PMU_CONF_NAME}.bin
}
