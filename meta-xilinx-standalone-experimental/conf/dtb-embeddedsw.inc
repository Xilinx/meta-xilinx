# The format of this file must be compatible with
# meta-xilinx/meta-xilinx-standalone/classes/xlnx-embeddedsw.bbclass

# Make it clear decoupling is 'experimental' in the version
ESW_VER = "2023_sdt_experimental"

REPO = "git://github.com/Xilinx/embeddedsw-experimental-dt-support.git;protocol=https"

ESW_BRANCH[2023_sdt_experimental] = "xlnx_rel_v2023.2_sdt"
ESW_REV[2023_sdt_experimental] = "742a608800e7621fb7c376daf5124333b5826d6d"
LIC_FILES_CHKSUM[master] = '7c92de7a21a6613265035c28f4a92f48'
LIC_FILES_CHKSUM[xlnx_rel_v2023.2_sdt] = 'ce611484168a6000bd35df68fc4f4290'
