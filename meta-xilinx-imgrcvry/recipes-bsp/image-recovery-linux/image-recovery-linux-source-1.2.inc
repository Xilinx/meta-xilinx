LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSES/MIT;md5=e8f57dd048e186199433be2c41bd3d6d"

REPO ??= "git://github.com/Xilinx/image-recovery-linux.git;protocol=https"

BRANCH ??= "main"

SRCREV ?= "5c354388034e3b1240e5f8b560a264f5ae8900a9"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"

SRC_URI = "${REPO};${BRANCHARG}"
