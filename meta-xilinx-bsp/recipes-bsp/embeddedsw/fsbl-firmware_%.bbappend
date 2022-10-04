# QEMU for the Kria SOM requires a section from the FSBL to be extracted

FSBL_DEFAULT_NAME = "executable.elf"
PMU_CONF_NAME = "pmu-conf.bin"

do_compile:append:zynqmp () {
    aarch64-none-elf-objcopy --dump-section .sys_cfg_data=../${PMU_CONF_NAME} ${FSBL_DEFAULT_NAME}
}

do_deploy:append:zynqmp () {
    install -Dm 0644 ${B}/${PMU_CONF_NAME} ${DEPLOYDIR}/${PMU_CONF_NAME}
}
