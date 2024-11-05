
# Automatically determnine the version from the bb file
ESW_VER ?= "${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or 'master'}"

REPO ??= "git://github.com/Xilinx/embeddedsw.git;protocol=https"

ESW_BRANCH[git] = "master"
ESW_BRANCH[2022.1] = "xlnx_rel_v2022.1_update"
ESW_BRANCH[2022.2] = "xlnx_rel_v2022.2"
ESW_BRANCH[2023.1] = "xlnx_rel_v2023.1_update"
ESW_BRANCH[2023.2] = "xlnx_rel_v2023.2_update"
ESW_BRANCH[2024.1] = "xlnx_rel_v2024.1"
ESW_BRANCH[2024.2] = "xlnx_rel_v2024.2"
BRANCH ??= "${@d.getVarFlag('ESW_BRANCH', d.getVar('ESW_VER')) or '${ESW_VER}'}"

ESW_REV[git] = "${AUTOREV}"
ESW_REV[2022.1] = "56d94a506fd9f80949f4cff08e13015928603f01"
ESW_REV[2022.2] = "5330a64c8efd14f0eef09befdbb8d3d738c33ec2"
ESW_REV[2023.1] = "af784f742dad0ca6e69e05baf8de51152c396b9a"
ESW_REV[2023.2] = "e847e1935dca630615e5f7dc694365a44b89699c"
ESW_REV[2024.1] = "b173d246826f662b9a98215d8f39e93d39d699b4"
ESW_REV[2024.2] = "6e4d0b89d2958994ab9b3531eb4c6e648a63f201"
SRCREV ??= "${@d.getVarFlag('ESW_REV', d.getVar('ESW_VER')) or 'INVALID'}"

EMBEDDEDSW_BRANCHARG ?= "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
EMBEDDEDSW_SRCURI ?= "${REPO};${EMBEDDEDSW_BRANCHARG}"

LICENSE = "MIT"
LIC_FILES_CHKSUM[master] = '9fceecdbcad88698f265578f3d4cb26c'
LIC_FILES_CHKSUM[xlnx_rel_v2022.1_update] = 'e62cb7a722c4430999e0a55a7234035d'
LIC_FILES_CHKSUM[xlnx_rel_v2022.2]  = 'ce611484168a6000bd35df68fc4f4290'
LIC_FILES_CHKSUM[xlnx_rel_v2023.1_update] = '3c310a3ee2197a4c92c6a0e2937c207c'
LIC_FILES_CHKSUM[xlnx_rel_v2023.2_update] = '9fceecdbcad88698f265578f3d4cb26c'
LIC_FILES_CHKSUM[xlnx_rel_v2024.1] = '0dcabd3719e5ac33f7c03f0d77d473f2'
LIC_FILES_CHKSUM[xlnx_rel_v2024.2] = '689662801a76c14d0cb57ae169cbec7c'
LIC_FILES_CHKSUM ??= "file://license.txt;md5=${@d.getVarFlag('LIC_FILES_CHKSUM', d.getVar('BRANCH')) or '0'}"

SRC_URI = "${EMBEDDEDSW_SRCURI}"
PV .= "+git"

python() {
    if d.getVar('BB_NO_NETWORK') == '1':
        try:
            # Just evaluating SRCPV / SRCREV can trigger an exception when BB_NO_NETWORK is enabled.
            var = d.getVar('SRCPV')
            var = d.getVar('SRCREV')
        except:
            raise bb.parse.SkipRecipe('BB_NO_NETWORK is enabled, can not fetch SRCREV (%s)' % d.getVar('SRCREV'))
}
