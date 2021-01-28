# Automatically determnine the version from the bb file
SRC_VER ?= "${@bb.parse.vars_from_file(d.getVar('FILE', False),d)[1] or 'master'}"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM[2019.1] = 'e9b6d01d45faccfbf05d8caea53f0a35'
LIC_FILES_CHKSUM[2019.2] = '39ab6ab638f4d1836ba994ec6852de94'
LIC_FILES_CHKSUM[2020.1] = '8b565227e1264d677db8f841c2948cba'
LIC_FILES_CHKSUM[2020.2] = '3a6e22aebf6516f0f74a82e1183f74f8'
LIC_FILES_CHKSUM[master] = '3a6e22aebf6516f0f74a82e1183f74f8'
LIC_FILES_CHKSUM = "file://license.txt;md5=${@d.getVarFlag('LIC_FILES_CHKSUM', d.getVar('SRC_VER')) or '0'}"

SRCREV[2019.1] = "26c14d9861010a0e3a55c73fb79efdb816eb42ca"
SRCREV[2019.2] = "e8db5fb118229fdc621e0ec7848641a23bf60998"
SRCREV[2020.1] = "338150ab3628a1ea6b06e964b16e712b131882dd"
SRCREV[2020.2] = "2516d5ed8161e16c2813b0e8e4ceac693f23de5c"
SRCREV ?= "${@d.getVarFlag('SRCREV', d.getVar('SRC_VER')) or '${AUTOREV}'}"

PV = "${SRC_VER}+git${SRCPV}"

SRC_BRANCH[2019.1] = "release-2019.1"
SRC_BRANCH[2019.2] = "release-2019.2"
SRC_BRANCH[2020.1] = "release-2020.1"
SRC_BRANCH[2020.2] = "master-rel-2020.2"
SRC_BRANCH ?= "${@d.getVarFlag('SRC_BRANCH', d.getVar('SRC_VER')) or '${SRC_VER}'}"

SRC_URI = "git://github.com/Xilinx/embeddedsw.git;protocol=https;branch=${SRC_BRANCH}"
