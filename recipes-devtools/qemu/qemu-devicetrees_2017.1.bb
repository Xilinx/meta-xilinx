SUMMARY = "Xilinx's hardware device trees required for QEMU"
HOMEPAGE = "https://github.com/xilinx/qemu-devicetrees/"
LICENSE = "BSD"
DEPENDS += "dtc-native"

inherit deploy

LIC_FILES_CHKSUM = "file://Makefile;beginline=1;endline=27;md5=7348b6cbcae69912cb1dee68d6c68d99"

BRANCH ?= ""
REPO ?= "git://github.com/Xilinx/qemu-devicetrees.git;protocol=https"
SRCREV ?= "1085e32a9ddc232963512923332094a58a05d1af"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

S = "${WORKDIR}/git"

# Don't need to do anything
do_install() {
	:
}

do_deploy() {
	for DTS_FILE in ${S}/LATEST/SINGLE_ARCH/*.dtb; do
		if [ ! -f ${DTS_FILE} ]; then
			bbwarn "${DTS_FILE} is not available!"
			continue
		fi
		DTS_NAME=`basename ${DTS_FILE} .dtb`
		install -d ${DEPLOYDIR}
		install -d ${DEPLOYDIR}/qemu-hw-devicetrees
		install -m 0644 ${S}/LATEST/SINGLE_ARCH/${DTS_NAME}.dtb ${DEPLOYDIR}/qemu-hw-devicetrees/${DTS_NAME}.dtb
	done
}

addtask deploy after do_install
