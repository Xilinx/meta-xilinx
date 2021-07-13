# The format of this file must be compatible with
# meta-xilinx/meta-xilinx-standalone/classes/xlnx-embeddedsw.bbclass

# Make it clear decoupling is 'experimental' in the version
ESW_VER = "experimental"

REPO = "git://gitenterprise.xilinx.com/decoupling/embeddedsw-experimental-dt-support.git;protocol=https"

ESW_BRANCH[experimental] = "master"
LIC_FILES_CHKSUM[master] = '7c92de7a21a6613265035c28f4a92f48'

