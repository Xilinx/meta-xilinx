SUMMARY = "Device Trees for BSPs"
DESCRIPTION = "Device Tree generation and packaging for BSP Device Trees."
SECTION = "bsp"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INHIBIT_DEFAULT_DEPS = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "dtc-native"

FILES_${PN} = "/boot/devicetree*"
DEVICETREE_FLAGS ?= "-R 8 -p 0x3000"

S = "${WORKDIR}"

do_compile() {
	if test -n "${MACHINE_DEVICETREE}"; then
		mkdir -p ${WORKDIR}/devicetree
		for i in ${MACHINE_DEVICETREE}; do
			if test -e ${WORKDIR}/$i; then
				echo cp ${WORKDIR}/$i ${WORKDIR}/devicetree
				cp ${WORKDIR}/$i ${WORKDIR}/devicetree
			fi
		done
	fi

	for DTS_FILE in ${DEVICETREE}; do
		DTS_NAME=`basename ${DTS_FILE} | awk -F "." '{print $1}'`
		dtc -I dts -O dtb ${DEVICETREE_FLAGS} -o ${DTS_NAME}.dtb ${DTS_FILE}
	done
}

do_install() {
	for DTS_FILE in ${DEVICETREE}; do
		if [ ! -f ${DTS_FILE} ]; then
			echo "Warning: ${DTS_FILE} is not available!"
			continue
		fi
		DTS_NAME=`basename ${DTS_FILE} | awk -F "." '{print $1}'`
		install -d ${D}/boot/devicetree
		install -m 0644 ${B}/${DTS_NAME}.dtb ${D}/boot/devicetree/${DTS_NAME}.dtb
	done
}

do_deploy() {
	for DTS_FILE in ${DEVICETREE}; do
		if [ ! -f ${DTS_FILE} ]; then
			echo "Warning: ${DTS_FILE} is not available!"
			continue
		fi
		DTS_NAME=`basename ${DTS_FILE} | awk -F "." '{print $1}'`
		install -d ${DEPLOY_DIR_IMAGE}
		install -m 0644 ${B}/${DTS_NAME}.dtb ${DEPLOY_DIR_IMAGE}/${DTS_NAME}.dtb
	done
}

# Deploy ${KERNEL_IMAGETYPE}-${DTS_NAME}.dtb for compatibility with runqemu
DEPLOY_KERNEL_DTB_qemuzynq = "1"
do_deploy_append() {
	if [ ! -z "${DEPLOY_KERNEL_DTB}" -a ! -z "${KERNEL_IMAGETYPE}" ]; then
		for DTS_FILE in ${DEVICETREE}; do
			DTS_NAME=`basename ${DTS_FILE} | awk -F "." '{print $1}'`
			KERNELDTBPATH=${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${DTS_NAME}.dtb
			if [ ! -e ${KERNELDTBPATH} -o -h ${KERNELDTBPATH} ]; then
				ln -sf ${DTS_NAME}.dtb ${KERNELDTBPATH}
			fi
		done
	fi
}

addtask deploy before do_build after do_install

inherit xilinx-utils

DEVICETREE ?= "${@expand_dir_basepaths_by_extension("MACHINE_DEVICETREE", os.path.join(d.getVar("WORKDIR", True), 'devicetree'), '.dts', d)}"
FILESEXTRAPATHS_append := "${@get_additional_bbpath_filespath('conf/machine/boards', d)}"

# Using the MACHINE_DEVICETREE and MACHINE_KCONFIG vars, append them to SRC_URI
SRC_URI += "${@paths_affix(d.getVar("MACHINE_DEVICETREE", True) or '', prefix = 'file://')}"

