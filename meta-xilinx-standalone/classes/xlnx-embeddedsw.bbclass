# Automatically determnine the version from the bb file
ESW_VER ?= "${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or 'master'}"

REPO ??= "git://github.com/Xilinx/embeddedsw.git;protocol=https"

ESW_BRANCH[2022.1] = "xlnx_rel_v2022.1_update"
ESW_BRANCH[2022.2] = "xlnx_rel_v2022.2"
ESW_BRANCH[2023.1] = "xlnx_rel_v2023.1"
BRANCH ??= "${@d.getVarFlag('ESW_BRANCH', d.getVar('ESW_VER')) or '${ESW_VER}'}"

ESW_REV[2022.1] = "56d94a506fd9f80949f4cff08e13015928603f01"
ESW_REV[2022.2] = "5330a64c8efd14f0eef09befdbb8d3d738c33ec2"
ESW_REV[2023.1] = "86f54b77641f325042a1101fead96b2714e6d3ef"
SRCREV ??= "${@d.getVarFlag('ESW_REV', d.getVar('ESW_VER')) or '${AUTOREV}'}"

EMBEDDEDSW_BRANCHARG ?= "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
EMBEDDEDSW_SRCURI ?= "${REPO};${EMBEDDEDSW_BRANCHARG}"

LICENSE = "MIT"
LIC_FILES_CHKSUM[xlnx_rel_v2022.1_update] = 'e62cb7a722c4430999e0a55a7234035d'
LIC_FILES_CHKSUM[xlnx_rel_v2022.2]  = 'ce611484168a6000bd35df68fc4f4290'
LIC_FILES_CHKSUM[xlnx_rel_v2023.1] = '3c310a3ee2197a4c92c6a0e2937c207c'
LIC_FILES_CHKSUM ??= "file://license.txt;md5=${@d.getVarFlag('LIC_FILES_CHKSUM', d.getVar('BRANCH')) or '0'}"

SRC_URI = "${EMBEDDEDSW_SRCURI}"
PV .= "+git${SRCPV}"
