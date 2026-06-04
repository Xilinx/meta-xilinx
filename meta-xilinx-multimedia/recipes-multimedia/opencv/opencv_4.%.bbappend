# Unified OpenCV .bbappend for Xilinx VDU, VCU, and VCU2 support
# This file conditionally adds video codec support based on machine features

VCU_VDU_INC = "${@bb.utils.contains_any("MACHINE_FEATURES", "vcu vcu2", "opencv_vcu_vdu.inc", "", d)}"

# TODO: opencv_vcu_vdu.inc fails to build on wrynose (contrib_xlnx path issues)
# require ${VCU_VDU_INC}
