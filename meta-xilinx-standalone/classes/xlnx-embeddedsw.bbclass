# Automatically determnine the version from the bb file
ESW_VER ?= "${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or 'master'}"

REPO ??= "git://github.com/Xilinx/embeddedsw.git;protocol=https"

ESW_BRANCH[2019.1] = "release-2019.1"
ESW_BRANCH[2019.2] = "release-2019.2"
ESW_BRANCH[2020.1] = "release-2020.1"
ESW_BRANCH[2020.2] = "master-rel-2020.2"
ESW_BRANCH[2021.1] = "xlnx_rel_v2021.1"
ESW_BRANCH[git]    = "xlnx_rel_v2021.1"
BRANCH ??= "${@d.getVarFlag('ESW_BRANCH', d.getVar('ESW_VER')) or '${ESW_VER}'}"

ESW_REV[2019.1] = "26c14d9861010a0e3a55c73fb79efdb816eb42ca"
ESW_REV[2019.2] = "e8db5fb118229fdc621e0ec7848641a23bf60998"
ESW_REV[2020.1] = "338150ab3628a1ea6b06e964b16e712b131882dd"
ESW_REV[2020.2] = "2516d5ed8161e16c2813b0e8e4ceac693f23de5c"
ESW_REV[2021.1] = "d37a0e8824182597abf31ac3f1087a5321b33ad7"
ESW_REV[git]    = "d37a0e8824182597abf31ac3f1087a5321b33ad7"
SRCREV ??= "${@d.getVarFlag('ESW_REV', d.getVar('ESW_VER')) or '${AUTOREV}'}"

EMBEDDEDSW_BRANCHARG ?= "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
EMBEDDEDSW_SRCURI ?= "${REPO};${EMBEDDEDSW_BRANCHARG}"

LICENSE = "MIT"
LIC_FILES_CHKSUM[release-2019.1]    = 'e9b6d01d45faccfbf05d8caea53f0a35'
LIC_FILES_CHKSUM[release-2019.2]    = '39ab6ab638f4d1836ba994ec6852de94'
LIC_FILES_CHKSUM[release-2020.1]    = '8b565227e1264d677db8f841c2948cba'
LIC_FILES_CHKSUM[master-rel-2020.2] = '3a6e22aebf6516f0f74a82e1183f74f8'
LIC_FILES_CHKSUM[xlnx_rel_v2021.1]  = "73e8997d53c2137fdeea4331a73f40fa"
LIC_FILES_CHKSUM[master]            = '73e8997d53c2137fdeea4331a73f40fa'
LIC_FILES_CHKSUM ??= "file://license.txt;md5=${@d.getVarFlag('LIC_FILES_CHKSUM', d.getVar('BRANCH')) or '0'}"

SRC_URI = "${EMBEDDEDSW_SRCURI}"
PV = "${ESW_VER}+git${SRCPV}"
