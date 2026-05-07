
# Automatically determnine the version from the bb file
ESW_VER ?= "${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or 'master'}"

REPO ??= "git://github.com/Xilinx/embeddedsw.git;protocol=https;name=embeddedsw"

ESW_BRANCH[git] = "master"
ESW_BRANCH[2022.1] = "xlnx_rel_v2022.1_update"
ESW_BRANCH[2022.2] = "xlnx_rel_v2022.2"
ESW_BRANCH[2023.1] = "xlnx_rel_v2023.1_update"
ESW_BRANCH[2023.2] = "xlnx_rel_v2023.2_update"
ESW_BRANCH[2024.1] = "xlnx_rel_v2024.1"
ESW_BRANCH[2024.2] = "xlnx_rel_v2024.2"
ESW_BRANCH[2025.1] = "xlnx_rel_v2025.1"
ESW_BRANCH[2025.2] = "xlnx_rel_v2025.2"
ESW_BRANCH[2026.1] = "xlnx_rel_v2026.1"
ESW_BRANCH = "${@d.getVarFlag('ESW_BRANCH', d.getVar('ESW_VER')) or '${ESW_VER}'}"
ESW_BRANCH[vardeps] += "ESW_VER"
BRANCH ??= "${ESW_BRANCH}"

ESW_REV[git] = "${AUTOREV}"
ESW_REV[2022.1] = "56d94a506fd9f80949f4cff08e13015928603f01"
ESW_REV[2022.2] = "5330a64c8efd14f0eef09befdbb8d3d738c33ec2"
ESW_REV[2023.1] = "af784f742dad0ca6e69e05baf8de51152c396b9a"
ESW_REV[2023.2] = "e847e1935dca630615e5f7dc694365a44b89699c"
ESW_REV[2024.1] = "b173d246826f662b9a98215d8f39e93d39d699b4"
ESW_REV[2024.2] = "6e4d0b89d2958994ab9b3531eb4c6e648a63f201"
ESW_REV[2025.1] = "cc89abdc1c394c19f0b72d5b498c0b5ed7403442"
ESW_REV[2025.2] = "145cea8fcf98268c8b163f732c181f008e887e53"
ESW_REV[2026.1] = "bb807c9f7b0be6e5e8d0c12ed3fa997c783043ae"
ESW_REV = "${@d.getVarFlag('ESW_REV', d.getVar('ESW_VER')) or 'INVALID'}"
ESW_REV[vardeps] += "ESW_VER"
SRCREV_embeddedsw ??= "${ESW_REV}"

EMBEDDEDSW_BRANCHARG ?= "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
EMBEDDEDSW_SRCURI ?= "${REPO};${EMBEDDEDSW_BRANCHARG}"

LICENSE = "MIT"
ESW_LIC_FILES_CHKSUM[master] = 'c225c4cbc07b5aceeb804f4abad3c693'
ESW_LIC_FILES_CHKSUM[xlnx_rel_v2022.1_update] = 'e62cb7a722c4430999e0a55a7234035d'
ESW_LIC_FILES_CHKSUM[xlnx_rel_v2022.2]  = 'ce611484168a6000bd35df68fc4f4290'
ESW_LIC_FILES_CHKSUM[xlnx_rel_v2023.1_update] = '3c310a3ee2197a4c92c6a0e2937c207c'
ESW_LIC_FILES_CHKSUM[xlnx_rel_v2023.2_update] = '9fceecdbcad88698f265578f3d4cb26c'
ESW_LIC_FILES_CHKSUM[xlnx_rel_v2024.1] = '0dcabd3719e5ac33f7c03f0d77d473f2'
ESW_LIC_FILES_CHKSUM[xlnx_rel_v2024.2] = '689662801a76c14d0cb57ae169cbec7c'
ESW_LIC_FILES_CHKSUM[xlnx_rel_v2025.1] = '981710c1c5161c0b8b578ede7374d1c9'
ESW_LIC_FILES_CHKSUM[xlnx_rel_v2025.2] = 'c225c4cbc07b5aceeb804f4abad3c693'
ESW_LIC_FILES_CHKSUM[xlnx_rel_v2026.1] = 'c225c4cbc07b5aceeb804f4abad3c693'
ESW_LIC_FILES_CHKSUM = "${@d.getVarFlag('ESW_LIC_FILES_CHKSUM', d.getVar('BRANCH')) or '0'}"
ESW_LIC_FILES_CHKSUM[vardeps] += "BRANCH"
LIC_FILES_CHKSUM ??= "file://license.txt;md5=${ESW_LIC_FILES_CHKSUM}"

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

SHARED_S = "${TMPDIR}/work-shared/embeddedsw-${ESW_VER}/git"
S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

ERROR_QA:remove = "buildpaths"

# The following is for recipes that use the common sources
python do_copy_shared_src() {
    src = d.getVar('SHARED_S')
    dest = d.getVar('S')
    if src != dest:
        oe.path.copyhardlinktree(src, dest)
}

python() {
    if d.getVar('BPN') != "embeddedsw-source":
        bb.build.addtask('do_copy_shared_src', 'do_configure do_populate_lic do_deploy_source_date_epoch', 'do_patch', d)

        d.appendVarFlag('do_copy_shared_src', 'depends', ' embeddedsw-source-${ESW_VER}:do_configure')

        d.appendVarFlag('do_deploy_source_date_epoch', 'depends', ' embeddedsw-source-${ESW_VER}:do_deploy_source_date_epoch')
}

do_deploy_source_date_epoch () {
    if [ "${BPN}" = "embeddedsw-source" ]; then
        # Stock behavior from classes-global/base.bbclass
        mkdir -p ${SDE_DEPLOYDIR}
        if [ -e ${SDE_FILE} ]; then
            echo "Deploying SDE from ${SDE_FILE} -> ${SDE_DEPLOYDIR}."
            cp -p ${SDE_FILE} ${SDE_DEPLOYDIR}/__source_date_epoch.txt
        else
            echo "${SDE_FILE} not found!"
        fi
    else
        # Shared workspace specific version, based on gcc-shared-source.inc
        sde_file=${SDE_FILE}
        sde_file=${sde_file#${WORKDIR}/}
        mkdir -p ${SDE_DEPLOYDIR} $(dirname ${SDE_FILE})
        cp -p $(dirname ${SHARED_S})/$sde_file ${SDE_DEPLOYDIR}
        cp -p $(dirname ${SHARED_S})/$sde_file ${SDE_FILE}
    fi
}
