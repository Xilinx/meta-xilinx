SUMMARY = "PMU ROM for QEMU"
DESCRIPTION = "The ZynqMP PMU ROM for QEMU emulation"
HOMEPAGE = "http://www.xilinx.com"
SECTION = "bsp"

# The BSP package does not include any license information.
LICENSE = "Proprietary"
LICENSE_FLAGS = "xilinx"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

COMPATIBLE_MACHINE = "zcu102-zynqmp"

inherit deploy
inherit xilinx-fetch-restricted

BSP_NAME = "xilinx-zcu102"
BSP_FILE = "${BSP_NAME}-v${PV}-final.bsp"
SRC_URI = "https://www.xilinx.com/member/forms/download/xef.html?filename=${BSP_FILE};downloadfilename=${BSP_FILE}"
SRC_URI[md5sum] = "b39c5de323cf43a44da2f6eaa7e44d43"
SRC_URI[sha256sum] = "12bd85350cad01ab646cb983c9fcbbe06c2014a7c1a61fe8c4a74fab518aa45d"

INHIBIT_DEFAULT_DEPS = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_compile() {
	# Extract the rom into workdir
	tar -xf ${WORKDIR}/${BSP_FILE} ${BSP_NAME}-${PV}/pre-built/linux/images/pmu_rom_qemu_sha3.elf -C ${S}
	# tar preserves the tree, so use find to get the full path and move to to the root
	for i in $(find ${S} -type f -name *.elf); do mv $i ${S}/pmu-rom.elf; done
}

do_install() {
	:
}

do_deploy () {
	install -D ${S}/pmu-rom.elf ${DEPLOYDIR}/pmu-rom.elf
}

addtask deploy before do_build after do_install

