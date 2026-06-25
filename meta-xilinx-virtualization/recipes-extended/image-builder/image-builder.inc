SUMMARY = "Xen Image Builder"
DESCRIPTION = "AMD Xilinx port of Xen's image-builder helper that \
takes a Xen DomU image plus configuration and produces the boot \
artifacts (ITS/ITB, U-Boot scripts, etc.) needed to launch it on AMD \
Xilinx Xen-based virtualization stacks."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d2794c0df5b907fdace235a619d80314"

REPO ?= "git://github.com/Xilinx/imagebuilder.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

S = "${WORKDIR}/git"

inherit deploy

DEPENDS += " \
    u-boot-mkimage-native \
    "

RDEPENDS:${PN} += " \
    bash \
    "

do_install () {
    install -d ${D}${bindir}
    chmod -R 777 ${S}/scripts/*
    install -m 0755 ${S}/scripts/* ${D}${bindir}
    ln -sf ${D}${bindir}/uboot-script-gen uboot-script-gen
}

do_deploy() {
	:
}

do_deploy:class-native() {
    install -d ${DEPLOYDIR}/scripts
    install -m 0755 ${S}/scripts/* ${DEPLOYDIR}/scripts/.
}

addtask do_deploy after do_install

BBCLASSEXTEND = "native nativesdk"
