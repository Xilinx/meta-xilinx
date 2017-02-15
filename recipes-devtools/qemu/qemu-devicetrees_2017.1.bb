SUMMARY = "Xilinx's hardware device trees required for QEMU"
HOMEPAGE = "https://github.com/xilinx/qemu-devicetrees/"
LICENSE = "BSD"
DEPENDS += "dtc-native"

inherit deploy

LIC_FILES_CHKSUM = "file://Makefile;beginline=1;endline=27;md5=7348b6cbcae69912cb1dee68d6c68d99"

BRANCH ?= ""
REPO ?= "git://github.com/Xilinx/qemu-devicetrees.git;protocol=https"
SRCREV ?= "0451d662f6cfe0abcb6570589f637786295b6a14"

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
		install -d ${DEPLOYDIR}/qemu-hw-devicetrees/SINGLE_ARCH
		install -m 0644 ${S}/LATEST/SINGLE_ARCH/${DTS_NAME}.dtb ${DEPLOYDIR}/qemu-hw-devicetrees/SINGLE_ARCH/${DTS_NAME}.dtb
		install -d ${DEPLOYDIR}/qemu-hw-devicetrees/MULTI_ARCH
		install -m 0644 ${S}/LATEST/MULTI_ARCH/${DTS_NAME}.dtb ${DEPLOYDIR}/qemu-hw-devicetrees/MULTI_ARCH/${DTS_NAME}.dtb
	done
}

addtask deploy after do_install
