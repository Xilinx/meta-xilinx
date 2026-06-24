LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSES/MIT;md5=e8f57dd048e186199433be2c41bd3d6d"

REPO ??= "git://github.com/Xilinx/image-recovery-linux.git;protocol=https"

BRANCH ?= "main"

SRCREV ?= "cabc85d26b001b66bb57bdd77b35b0a74741db98"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"

SRC_URI = "${REPO};${BRANCHARG}"
