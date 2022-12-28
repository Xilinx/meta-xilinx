# The format of this file must be compatible with
# meta-xilinx/meta-xilinx-standalone/classes/xlnx-embeddedsw.bbclass

# Make it clear decoupling is 'experimental' in the version
ESW_VER = "experimental"

REPO = "git://gitenterprise.xilinx.com/decoupling/embeddedsw-experimental-dt-support.git;protocol=https"

ESW_BRANCH[experimental] = "xlnx_rel_v2023.1_sdt_experimental"
ESW_REV[experimental] = "249231d2140e7786469e3538507191d506d391a6"
LIC_FILES_CHKSUM[master] = '7c92de7a21a6613265035c28f4a92f48'
LIC_FILES_CHKSUM[xlnx_rel_v2022.1_sdt_experimental_beta] = 'e26f53a7d6f58f4b1a9687099417225c'
LIC_FILES_CHKSUM[xlnx_rel_v2023.1_sdt_experimental] = 'ce611484168a6000bd35df68fc4f4290'
