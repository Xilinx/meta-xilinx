# Automatically determnine the version from the bb file
ESW_VER ?= "${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or 'master'}"

REPO ??= "git://github.com/Xilinx/embeddedsw.git;protocol=https"

ESW_BRANCH[git] = "master"
ESW_BRANCH[2023.1] = "xlnx_rel_v2023.1_update"
ESW_BRANCH[2023.2] = "xlnx_rel_v2023.2_update"
ESW_BRANCH[2024.1] = "master-next"
BRANCH ??= "${@d.getVarFlag('ESW_BRANCH', d.getVar('ESW_VER')) or '${ESW_VER}'}"

ESW_REV[git] = "${AUTOREV}"
ESW_REV[2023.1] = "af784f742dad0ca6e69e05baf8de51152c396b9a"
ESW_REV[2023.2] = "73f0904e41cc109f18bb19a5329d0e5a66af2434"
ESW_REV[2024.1] = "7a819824db758891fede738bd35170c54473e954"
SRCREV ??= "${@d.getVarFlag('ESW_REV', d.getVar('ESW_VER')) or '${AUTOREV}'}"

EMBEDDEDSW_BRANCHARG ?= "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
EMBEDDEDSW_SRCURI ?= "${REPO};${EMBEDDEDSW_BRANCHARG}"

LICENSE = "MIT"
LIC_FILES_CHKSUM[master] = '9fceecdbcad88698f265578f3d4cb26c'
LIC_FILES_CHKSUM[xlnx_rel_v2023.1_update] = '3c310a3ee2197a4c92c6a0e2937c207c'
LIC_FILES_CHKSUM[xlnx_rel_v2023.2_update] = '9fceecdbcad88698f265578f3d4cb26c'
LIC_FILES_CHKSUM[master-next] = '9fceecdbcad88698f265578f3d4cb26c'
LIC_FILES_CHKSUM ??= "file://license.txt;md5=${@d.getVarFlag('LIC_FILES_CHKSUM', d.getVar('BRANCH')) or '0'}"

SRC_URI = "${EMBEDDEDSW_SRCURI}"
PV .= "+git${SRCPV}"
