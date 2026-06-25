SUMMARY = "Xilinx's hardware device trees required for QEMU"
DESCRIPTION = "Hand-written QEMU machine device trees that describe \
AMD Xilinx Zynq, Zynq UltraScale+ MPSoC, Versal and Versal NET \
reference platforms; consumed by the AMD QEMU fork to model the SoC IP \
topology when emulating these devices."
HOMEPAGE = "https://github.com/xilinx/qemu-devicetrees/"
LICENSE = "BSD-3-Clause"
DEPENDS += "dtc-native"

inherit deploy

PV .= "+git"

REPO ?= "git://github.com/Xilinx/qemu-devicetrees.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

SRC_URI += "file://0001-versal-Reorder-serial-ports.patch"
SRC_URI += "file://0001-versal-net-Reorder-serial-port.patch"

S = "${WORKDIR}/git"

# Don't do anything for target or native
do_install() {
	:
}

# Nativesdk bundle the device trees for the SDK
do_install:class-nativesdk() {
	# single-arch dtbs
	for DTS_FILE in ${S}/LATEST/SINGLE_ARCH/*.dtb; do
		install -Dm 0644 $DTS_FILE ${D}/${base_prefix}/qemu-hw-devicetrees/$(basename $DTS_FILE .dtb).dtb
	done

	# multi-arch dtbs
	for DTS_FILE in ${S}/LATEST/MULTI_ARCH/*.dtb; do
		install -Dm 0644 $DTS_FILE ${D}/${base_prefix}/qemu-hw-devicetrees/multiarch/$(basename $DTS_FILE .dtb).dtb
	done
}

do_deploy:class-native() {
	# single-arch dtbs
	for DTS_FILE in ${S}/LATEST/SINGLE_ARCH/*.dtb; do
		install -Dm 0644 $DTS_FILE ${DEPLOYDIR}/qemu-hw-devicetrees/$(basename $DTS_FILE .dtb).dtb
	done

	# multi-arch dtbs
	for DTS_FILE in ${S}/LATEST/MULTI_ARCH/*.dtb; do
		install -Dm 0644 $DTS_FILE ${DEPLOYDIR}/qemu-hw-devicetrees/multiarch/$(basename $DTS_FILE .dtb).dtb
	done
}

# Disable deploy in nativesdk!
do_deploy() {
	:
}

FILES:${PN}:class-nativesdk = "${base_prefix}/qemu-hw-devicetrees"

addtask deploy after do_install before do_build

COMPATIBLE_HOST:class-target = "none"
BBCLASSEXTEND = "native nativesdk"
