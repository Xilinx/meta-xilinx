# The format of this file must be compatible with
# meta-xilinx/meta-xilinx-standalone/classes/xlnx-embeddedsw.bbclass

# Make it clear decoupling is 'experimental' in the version
ESW_VER = "experimental"

REPO = "git://gitenterprise.xilinx.com/decoupling/embeddedsw.git;protocol=https"

ESW_BRANCH[experimental] = "master"
LIC_FILES_CHKSUM[master] = '3a6e22aebf6516f0f74a82e1183f74f8'

