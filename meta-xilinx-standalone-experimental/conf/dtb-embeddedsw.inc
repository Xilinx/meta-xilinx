# The format of this file must be compatible with
# meta-xilinx/meta-xilinx-standalone/classes/xlnx-embeddedsw.bbclass

# Make it clear decoupling is 'experimental' in the version
ESW_VER = "experimental"

REPO = "git://gitenterprise.xilinx.com/decoupling/embeddedsw-experimental-dt-support.git;protocol=https"

ESW_BRANCH[experimental] = "xilinx-v2022.1-sdt-experimental"
ESW_REV[experimental] = "a2d3e16376898780a75719688f2254349ae98962"
LIC_FILES_CHKSUM[master] = '7c92de7a21a6613265035c28f4a92f48'
LIC_FILES_CHKSUM[xilinx-v2022.1-sdt-experimental] = '1d552a9ac3973bf5fcff6fb9cb2034ea'
