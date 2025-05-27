DESCRIPTION = "Recipe to deploy cdo files from System Device Tree Directory"

LICENSE = "CLOSED"

inherit deploy

DEPENDS += "${SYSTEM_DTFILE_DEPENDS}"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = "versal"
COMPATIBLE_MACHINE:versal-2ve-2vm = "versal-2ve-2vm"
COMPATIBLE_MACHINE:versal-net = "versal-net"

# Since we're just copying, we can run any config
COMPATIBLE_HOST = ".*"

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

do_compile[noexec] = '1'
do_install[noexec] = '1'

do_deploy() {
    install -d ${DEPLOYDIR}/CDO
    pmc_data_cdo=$(find ${SYSTEM_DTFILE_DIR}/extracted/*/pdi_files/gen_files/ -name pmc_data.cdo | head -1)
    if [ -e $pmc_data_cdo ]; then
        install -m 0644 ${pmc_data_cdo} ${DEPLOYDIR}/CDO/pmc_data.cdo
    fi

    lpd_data_cdo=$(find ${SYSTEM_DTFILE_DIR}/extracted/*/pdi_files/gen_files/ -name lpd_data.cdo | head -1)
    if [ -e $lpd_data_cdo ]; then
        install -m 0644 $lpd_data_cdo ${DEPLOYDIR}/CDO/lpd_data.cdo
    fi
}
addtask deploy before do_build after do_install
