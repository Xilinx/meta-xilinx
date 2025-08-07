DESCRIPTION = "Image Recovery"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

REPO ??= "git://github.com/Xilinx/image-recovery-linux.git;protocol=https"
BRANCH ??= "main"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"

SRC_URI = "${REPO};${BRANCHARG}"
SRCREV ??= "06cc33a6ca3d66e810cbac01b644ad7215e3c3b5"

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
