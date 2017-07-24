LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCREV = "cdfe336d88c31ba882b37e36a69fb4762332540b"
REPO ?= "git://gitenterprise.xilinx.com/uspea/uspea.git;protocol=https"
BRANCH ?= "master"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS = "rfdc"
RDEPENDS_${PN} = "rfdc"

PACKAGES = "${PN}-dbg ${PN}"

S = "${WORKDIR}/git/rfdc/examples/${PN}"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${PN} ${D}${bindir}
}
