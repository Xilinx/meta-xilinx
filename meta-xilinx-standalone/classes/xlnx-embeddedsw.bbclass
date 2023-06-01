# Automatically determnine the version from the bb file
ESW_VER ?= "${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or 'master'}"

REPO ??= "git://github.com/Xilinx/embeddedsw.git;protocol=https"

ESW_BRANCH[2023.1] = "xlnx_rel_v2023.1"
ESW_BRANCH[2023.2_ksb] = "xlnx_rel_v2023.2_ksb"
ESW_BRANCH[2023.2-ksb] := "${@d.getVarFlag('ESW_BRANCH', '2023.2_ksb')}"
BRANCH ??= "${@d.getVarFlag('ESW_BRANCH', d.getVar('ESW_VER')) or '${ESW_VER}'}"

ESW_REV[2023.1] = "86f54b77641f325042a1101fead96b2714e6d3ef"
ESW_REV[2023.2_ksb] = "5153b2dae3b72746562888151c22d3fbad51d79e"
ESW_REV[2023.2-ksb] := "${@d.getVarFlag('ESW_REV', '2023.2_ksb')}"
SRCREV ??= "${@d.getVarFlag('ESW_REV', d.getVar('ESW_VER')) or '${AUTOREV}'}"

EMBEDDEDSW_BRANCHARG ?= "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
EMBEDDEDSW_SRCURI ?= "${REPO};${EMBEDDEDSW_BRANCHARG}"

LICENSE = "MIT"
LIC_FILES_CHKSUM[xlnx_rel_v2023.1] = '3c310a3ee2197a4c92c6a0e2937c207c'
LIC_FILES_CHKSUM[xlnx_rel_v2023.2_ksb] = '15386ea7656d3b83815bce88c0bbe66d'
LIC_FILES_CHKSUM ??= "file://license.txt;md5=${@d.getVarFlag('LIC_FILES_CHKSUM', d.getVar('BRANCH')) or '0'}"

SRC_URI = "${EMBEDDEDSW_SRCURI}"
PV .= "+git${SRCPV}"
