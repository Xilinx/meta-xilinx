COMPATIBLE_HOST_xilinx-standalone = "${HOST_SYS}"

COMPATIBLE_MACHINE_cortexa53-zynqmp = ".*"
COMPATIBLE_MACHINE_cortexr5-zynqmp = ".*"
COMPATIBLE_MACHINE:microblaze-pmu = ".*"
COMPATIBLE_MACHINE:microblaze-plm = ".*"
COMPATIBLE_MACHINE_cortexa72-versal = ".*"
COMPATIBLE_MACHINE_cortexr5-versal = ".*"
COMPATIBLE_MACHINE_cortexa9-zynq = ".*"

# Enable @ flag on dtc which is required by libxil
DTC_FLAGS:append_xilinx-standalone = " -@"
DT_INCLUDE:append_xilinx-standalone = " ${WORKDIR}/git/device_tree/data/kernel_dtsi/${XILINX_RELEASE_VERSION}/include/"
