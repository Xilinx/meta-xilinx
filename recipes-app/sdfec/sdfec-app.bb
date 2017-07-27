SUMMARY = "SDFEC APP"
SECTION = "sdfec/apps"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=1144d32a6544430840de1993867256dd"

XSDFEC_VERSION = "1.0.0"
PV = "${XSDFEC_VERSION}"
S = "${WORKDIR}/git/sdfec-app/files"

REPO ?= "git://gitenterprise.xilinx.com/uspea/sdfec-app.git;protocol=https"
BRANCH ?= "master"
SRCREV ?= "b97979976fb39ba79ee39cbe574a9d7adb32ddeb"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${PN} ${D}${bindir}
}

