# The format of this file must be compatible with
# meta-xilinx/meta-xilinx-standalone/classes/xlnx-embeddedsw.bbclass

# Make it clear decoupling is 'experimental' in the version
ESW_VER = "2023.1_sdt_experimental"

REPO = "git://github.com/Xilinx/embeddedsw-experimental-dt-support.git;protocol=https"

ESW_BRANCH[2023.1_sdt_experimental] = "xlnx_rel_v2023.1_sdt"
ESW_REV[2023.1_sdt_experimental] = "b47bfef27d7af0690add590b9c98e6fc51a1a1f6"
LIC_FILES_CHKSUM[master] = '7c92de7a21a6613265035c28f4a92f48'
LIC_FILES_CHKSUM[xlnx_rel_v2022.1_sdt_experimental_beta] = 'e26f53a7d6f58f4b1a9687099417225c'
LIC_FILES_CHKSUM[xlnx_rel_v2023.1_sdt_experimental] = 'ce611484168a6000bd35df68fc4f4290'
LIC_FILES_CHKSUM[xlnx_rel_v2023.1_sdt] = 'ce611484168a6000bd35df68fc4f4290'
