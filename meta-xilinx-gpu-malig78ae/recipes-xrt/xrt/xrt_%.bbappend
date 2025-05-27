PACKAGECONFIG[libmali-xlnx] = ",,libmali-xlnx,libmali-xlnx"

# Emulates OPENCL_ICD_LOADER
TARGET_CXXFLAGS .= "${@bb.utils.contains('PACKAGECONFIG', 'libmali-xlnx', ' -DOPENCL_ICD_LOADER=on', '', d)}"
