LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSES/MIT;md5=e8f57dd048e186199433be2c41bd3d6d"

REPO ??= "git://github.com/Xilinx/image-recovery-linux.git;protocol=https"

BRANCH ?= "main"

SRCREV ?= "0473a376a6e5c2510188d011d998c4472febc258"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"

SRC_URI = "${REPO};${BRANCHARG}"
