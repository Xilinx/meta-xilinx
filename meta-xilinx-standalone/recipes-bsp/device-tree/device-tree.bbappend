COMPATIBLE_HOST:xilinx-standalone = "${HOST_SYS}"
COMPATIBLE_HOST:xilinx-freertos = "${HOST_SYS}"

# Enable @ flag on dtc which is required by libxil
DTC_FLAGS:append:xilinx-standalone = " -@"
DT_INCLUDE:append:xilinx-standalone = " ${UNPACKDIR}/git/device_tree/data/kernel_dtsi/${XILINX_RELEASE_VERSION}/include/"
DTC_FLAGS:append:xilinx-freertos = " -@"
DT_INCLUDE:append:xilinx-freertos = " ${UNPACKDIR}/git/device_tree/data/kernel_dtsi/${XILINX_RELEASE_VERSION}/include/"
