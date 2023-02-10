COMPATIBLE_HOST:xilinx-standalone = "${HOST_SYS}"
COMPATIBLE_HOST:xilinx-freertos = "${HOST_SYS}"

COMPATIBLE_MACHINE:cortexa53-zynqmp = ".*"
COMPATIBLE_MACHINE:cortexr5-zynqmp = ".*"
COMPATIBLE_MACHINE:microblaze-pmu = ".*"
COMPATIBLE_MACHINE:microblaze-plm = ".*"
COMPATIBLE_MACHINE:cortexa72-versal = ".*"
COMPATIBLE_MACHINE:cortexr5-versal = ".*"
COMPATIBLE_MACHINE:cortexa9-zynq = ".*"

# Enable @ flag on dtc which is required by libxil
DTC_FLAGS:append:xilinx-standalone = " -@"
DT_INCLUDE:append:xilinx-standalone = " ${WORKDIR}/git/device_tree/data/kernel_dtsi/${XILINX_RELEASE_VERSION}/include/"
DTC_FLAGS:append:xilinx-freertos = " -@"
DT_INCLUDE:append:xilinx-freertos = " ${WORKDIR}/git/device_tree/data/kernel_dtsi/${XILINX_RELEASE_VERSION}/include/"
