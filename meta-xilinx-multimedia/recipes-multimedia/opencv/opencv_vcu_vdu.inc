# opencv_4.%.bbappend conditionally includes this file to add video codec support based on machine features
# Due to this, if MACHINE_FEATURES includes vdu, vcu or vdu2 this recipe is now machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += " git://github.com/Xilinx/opencv_modules_xlnx.git;protocol=https;branch=main;name=xlnx-modules;destsuffix=git/contrib_xlnx"

# Update this hash when you make changes to the opencv_modules_xlnx repository
SRCREV_xlnx-modules = "6eb9d4a23c0401f20a0f6802c9805af898d7b6c4"

# This extends the existing OPENCV_EXTRA_MODULES_PATH to include both official contrib and contrib_xlnx modules
EXTRA_OECMAKE += "-DOPENCV_EXTRA_MODULES_PATH='${S}/contrib/modules;${S}/contrib_xlnx/modules'"

# Conditionally enable vcucodec module based on machine features
# Only add the configuration if the machine has the corresponding feature

# VDU support - for machines with 'vdu' feature
PACKAGECONFIG:append = "${@bb.utils.contains('MACHINE_FEATURES', 'vdu', ' vcucodec-vdu', '', d)}"
PACKAGECONFIG[vcucodec-vdu] = "-DBUILD_opencv_vcucodec=ON -DHAVE_VDU_CTRLSW=1 -DBUILD_TESTS=OFF -DBUILD_PERF_TESTS=OFF,,vdu-ctrlsw,vdu-ctrlsw"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'vdu', 'vdu-ctrlsw', '', d)}"

# VCU support - for machines with 'vcu' feature
PACKAGECONFIG:append = "${@bb.utils.contains('MACHINE_FEATURES', 'vcu', ' vcucodec-vcu', '', d)}"
PACKAGECONFIG[vcucodec-vcu] = "-DBUILD_opencv_vcucodec=ON -DHAVE_VCU_CTRLSW=1 -DBUILD_TESTS=OFF -DBUILD_PERF_TESTS=OFF,,vcu-ctrlsw,vcu-ctrlsw"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'vcu', 'vcu-ctrlsw', '', d)}"

# VCU2 support - for machines with 'vcu2' feature
PACKAGECONFIG:append = "${@bb.utils.contains('MACHINE_FEATURES', 'vcu2', ' vcucodec-vcu2', '', d)}"
PACKAGECONFIG[vcucodec-vcu2] = "-DBUILD_opencv_vcucodec=ON -DHAVE_VCU2_CTRLSW=1 -DBUILD_TESTS=OFF -DBUILD_PERF_TESTS=OFF,,vcu2-ctrlsw,vcu2-ctrlsw"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'vcu2', 'vcu2-ctrlsw', '', d)}"
