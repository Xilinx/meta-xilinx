COMPATIBLE_MACHINE_cortexa53-zynqmp = ".*"
COMPATIBLE_MACHINE_cortexr5-zynqmp = ".*"
COMPATIBLE_MACHINE_microblaze-pmu = ".*"
COMPATIBLE_MACHINE_microblaze-plm = ".*"
COMPATIBLE_MACHINE_cortexa72-versal = ".*"

REPO_cortexa53-zynqmp = "git://gitenterprise.xilinx.com/decoupling/device-tree-xlnx;protocol=https"
REPO_cortexr5-zynqmp = "git://gitenterprise.xilinx.com/decoupling/device-tree-xlnx;protocol=https"
REPO_microblaze-pmu = "git://gitenterprise.xilinx.com/decoupling/device-tree-xlnx;protocol=https"
REPO_microblaze-plm = "git://gitenterprise.xilinx.com/decoupling/device-tree-xlnx;protocol=https"
REPO_cortexa72-versal = "git://gitenterprise.xilinx.com/decoupling/device-tree-xlnx;protocol=https"

BRANCH = "master-next-test"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

SRCREV = "${AUTOREV}"

XSCTH_PROC_cortexa53-zynqmp ??= "psu_cortexa53_0"
XSCTH_PROC_cortexr5-zynqmp ??= "psu_cortexr5_0"
XSCTH_PROC_microblaze-pmu ??= "psu_pmu_0"
XSCTH_PROC_cortexa72-versal ??= "psv_cortexa72_0"
XSCTH_PROC_microblaze-plm ??= "psv_pmc_0"

# Enable @ flag on dtc which is required by libxil
DTC_FLAGS_append = " -@"
DT_INCLUDE_append = " ${WORKDIR}/git/device_tree/data/kernel_dtsi/2019.2/include/"


do_install_append_cortexa53-zynqmp(){
    install -d ${D}${includedir}/devicetree
    install -m 0644 ${B}/${PN}/psu_init.c ${D}/${includedir}/devicetree/psu_init.c
    install -m 0644 ${B}/${PN}/psu_init.h ${D}/${includedir}/devicetree/psu_init.h
}
