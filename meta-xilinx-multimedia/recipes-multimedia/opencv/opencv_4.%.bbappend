# Unified OpenCV .bbappend for Xilinx VDU, VCU, and VCU2 support
# This file conditionally adds video codec support based on machine features

VCU_VDU_INC = "${@bb.utils.contains_any("MACHINE_FEATURES", "vcu vcu2", "opencv_vcu_vdu.inc", "", d)}"

require ${VCU_VDU_INC}