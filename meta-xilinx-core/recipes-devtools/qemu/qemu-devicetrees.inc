SUMMARY = "Xilinx's hardware device trees required for QEMU"
HOMEPAGE = "https://github.com/xilinx/qemu-devicetrees/"
LICENSE = "BSD-3-Clause"
DEPENDS += "dtc-native"

inherit deploy

LIC_FILES_CHKSUM = "file://Makefile;beginline=1;endline=27;md5=7348b6cbcae69912cb1dee68d6c68d99"

PV .= "+git"

REPO ?= "git://github.com/Xilinx/qemu-devicetrees.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

SRC_URI += "file://0001-versal-Reorder-serial-ports.patch"
SRC_URI += "file://0001-versal-net-Reorder-serial-port.patch"

S = "${WORKDIR}/git"

do_install[noexec] = '1'

do_deploy() {
	# single-arch dtbs
	for DTS_FILE in ${S}/LATEST/SINGLE_ARCH/*.dtb; do
		install -Dm 0644 $DTS_FILE ${DEPLOYDIR}/qemu-hw-devicetrees/$(basename $DTS_FILE .dtb).dtb
	done

	# multi-arch dtbs
	for DTS_FILE in ${S}/LATEST/MULTI_ARCH/*.dtb; do
		install -Dm 0644 $DTS_FILE ${DEPLOYDIR}/qemu-hw-devicetrees/multiarch/$(basename $DTS_FILE .dtb).dtb
	done
}

addtask deploy after do_install before do_build

COMPATIBLE_HOST:class-target = "none"
BBCLASSEXTEND = "native nativesdk"
