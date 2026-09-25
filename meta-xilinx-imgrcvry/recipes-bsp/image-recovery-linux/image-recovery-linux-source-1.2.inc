LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSES/MIT;md5=e8f57dd048e186199433be2c41bd3d6d"

REPO ??= "git://github.com/Xilinx/image-recovery-linux.git;protocol=https"

BRANCH ??= "main"

SRCREV ?= "a09850a3082d252489f606b8acb12ab8d42ef48d"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"

SRC_URI = "${REPO};${BRANCHARG}"
