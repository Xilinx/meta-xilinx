SUMMARY = "Xilinx's hardware device trees required for QEMU"
HOMEPAGE = "https://github.com/xilinx/qemu-devicetrees/"
LICENSE = "BSD"
DEPENDS += "dtc-native"

inherit deploy

LIC_FILES_CHKSUM = "file://Makefile;beginline=1;endline=27;md5=7348b6cbcae69912cb1dee68d6c68d99"

SRCREV = "46faf58cd14cdfd06cae7c076cb486af8565ab6a"
SRC_URI = "git://github.com/Xilinx/qemu-devicetrees.git;protocol=https;nobranch=1"

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
		DTS_NAME=`basename -s .dtb ${DTS_FILE}`
		install -d ${DEPLOYDIR}
		install -d ${DEPLOYDIR}/qemu-hw-devicetrees
		install -m 0644 ${S}/LATEST/SINGLE_ARCH/${DTS_NAME}.dtb ${DEPLOYDIR}/qemu-hw-devicetrees/${DTS_NAME}.dtb
	done
}

addtask deploy after do_install
