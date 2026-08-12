LMSENSOR_ADDITIONAL_INCLUDE = ""

# Make the changes only apply to AMD FPGA boards
LMSENSOR_ADDITIONAL_INCLUDE:zynqmp         = "amd-fpga-fancontrol.inc"
LMSENSOR_ADDITIONAL_INCLUDE:versal         = "amd-fpga-fancontrol.inc"
LMSENSOR_ADDITIONAL_INCLUDE:versal-net     = "amd-fpga-fancontrol.inc"
LMSENSOR_ADDITIONAL_INCLUDE:versal-2ve-2vm = "amd-fpga-fancontrol.inc"

require ${LMSENSOR_ADDITIONAL_INCLUDE}
