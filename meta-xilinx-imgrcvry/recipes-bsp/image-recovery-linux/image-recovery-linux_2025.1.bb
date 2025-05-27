DESCRIPTION = "Image Recovery"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

REPO ??= "git://github.com/Xilinx/image-recovery-linux.git;protocol=https"
BRANCH ??= "main"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"

SRC_URI = "${REPO};${BRANCHARG}"
SRCREV ??= "71b46904f3a4ef5e8402da13a91365756407b1de"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	# install image-recovery-linux files
	install -d ${D}${localstatedir}
	install -d ${D}${localstatedir}/imgrcry_web/
	cp -rf ${S}/* ${D}${localstatedir}/imgrcry_web/
	chmod -R 775 ${D}${localstatedir}/imgrcry_web/cgi-bin
}
