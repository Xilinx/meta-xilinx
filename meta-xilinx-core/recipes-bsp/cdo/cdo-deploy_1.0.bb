SUMMARY = "Deploys CDO (Configuration Data Object) files extracted \
from the System Device Tree directory for AMD Versal targets."
DESCRIPTION = "Recipe to deploy cdo files from System Device Tree \
Directory"

# Deploys data generated from the user's design, which can't be interpreted
LICENSE = "LicenseRef-Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

inherit deploy

DEPENDS += "${SYSTEM_DTFILE_DEPENDS}"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = "versal"
COMPATIBLE_MACHINE:versal-net = "versal-net"
COMPATIBLE_MACHINE:versal-2ve-2vm = "versal-2ve-2vm"
COMPATIBLE_MACHINE:versal-2vp = "versal-2vp"

# Since we're just copying, we can run any config
COMPATIBLE_HOST = ".*"

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

do_compile[noexec] = '1'
do_install[noexec] = '1'

do_deploy() {
    install -d ${DEPLOYDIR}/CDO
    for cdo in pmc_data lpd_data fpd_data; do
        src=$(find ${SYSTEM_DTFILE_DIR}/extracted/*/pdi_files/gen_files/ -name "$cdo.cdo" | head -1)
        if [ -n "$src" ]; then
            install -m 0644 "$src" ${DEPLOYDIR}/CDO/$cdo.cdo
        fi
    done
}
addtask deploy before do_build after do_install
