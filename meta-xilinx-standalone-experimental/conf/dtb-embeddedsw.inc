# The format of this file must be compatible with
# meta-xilinx/meta-xilinx-standalone/classes/xlnx-embeddedsw.bbclass

# Make it clear decoupling is 'experimental' in the version
ESW_VER = "experimental"

REPO = "git://gitenterprise.xilinx.com/decoupling/embeddedsw-experimental-dt-support.git;protocol=https"

ESW_BRANCH[experimental] = "xilinx-v2022.1-sdt-experimental"
ESW_REV[experimental] = "b4727281ed58d095cfa758eaca5f556b485c932f"
LIC_FILES_CHKSUM[master] = '7c92de7a21a6613265035c28f4a92f48'
LIC_FILES_CHKSUM[xilinx-v2022.1-sdt-experimental] = 'e26f53a7d6f58f4b1a9687099417225c'
