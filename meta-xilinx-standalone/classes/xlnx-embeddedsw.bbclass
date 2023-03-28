# Automatically determnine the version from the bb file
ESW_VER ?= "${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or 'master'}"

REPO ??= "git://github.com/Xilinx/embeddedsw.git;protocol=https"

ESW_BRANCH[2023.1] = "xlnx_rel_v2023.1-next"
ESW_BRANCH[2023.2] = "master-next"
BRANCH ??= "${@d.getVarFlag('ESW_BRANCH', d.getVar('ESW_VER')) or '${ESW_VER}'}"

ESW_REV[2023.1] = "2e9f85579e203d76b983ff21c4294a8c3d578942"
ESW_REV[2023.2] = "c5960021b8db5f6bfd15e036e8719a6734af67f1"
SRCREV ??= "${@d.getVarFlag('ESW_REV', d.getVar('ESW_VER')) or '${AUTOREV}'}"

EMBEDDEDSW_BRANCHARG ?= "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
EMBEDDEDSW_SRCURI ?= "${REPO};${EMBEDDEDSW_BRANCHARG}"

LICENSE = "MIT"
LIC_FILES_CHKSUM[xlnx_rel_v2023.1-next] = '15386ea7656d3b83815bce88c0bbe66d'
LIC_FILES_CHKSUM[master-next] = '15386ea7656d3b83815bce88c0bbe66d'
LIC_FILES_CHKSUM ??= "file://license.txt;md5=${@d.getVarFlag('LIC_FILES_CHKSUM', d.getVar('BRANCH')) or '0'}"

SRC_URI = "${EMBEDDEDSW_SRCURI}"
PV .= "+git${SRCPV}"
