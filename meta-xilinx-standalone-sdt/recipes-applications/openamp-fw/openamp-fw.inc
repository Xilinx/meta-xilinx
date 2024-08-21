LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
LICENSE = "MIT"
INSANE_SKIP:${PN} = "arch"

COMPATIBLE_HOST = ".*"

FW_LIB_DIR = "${nonarch_base_libdir}/firmware"
INSANE_SKIP:${PN} = "arch"

OPENAMP_FW_SRC_NAME ?= "rpmsg-echo.out"
OPENAMP_FW_NAME ?= "image_echo_test"

FW_MACHINE:zynqmp = "cortexr5-0"
FW_MACHINE:versal = "cortexr5-0"
FW_MACHINE:versal-net = "cortexr52-0"
FW_OS ?= "baremetal"

FW_TARGET ?= "${MACHINE}-${FW_MACHINE}-${FW_OS}"

OPENAMP_FW_DEPLOY_DIR ?= "-${FW_TARGET}/deploy/images/${MACHINE}/"

DEPENDS = " open-amp-xlnx "
do_install[mcdepends] += "mc::${FW_TARGET}:open-amp-xlnx:do_deploy"
do_install() {
	src=${TMPDIR}${OPENAMP_FW_DEPLOY_DIR}/${OPENAMP_FW_SRC_NAME}
	dest=${D}${FW_LIB_DIR}/${OPENAMP_FW_NAME}
	if [ ! -e $src ]; then
		bberror "Multiconfig firmware $src is not found.  Set OPENAMP_FW_DEPLOY_DIR:pn-${PN} if necessary."
		exit 1
	fi
	install -d ${D}${FW_LIB_DIR}
	install -m 0644 ${src} ${dest}
}

do_compile[noexec] = "1"

FILES:${PN} = "${FW_LIB_DIR}/${OPENAMP_FW_NAME}"

python() {
    multiconfigs = (d.getVar('BBMULTICONFIG') or '').split()
    fw_target = d.getVar('FW_TARGET')
    if not fw_target in multiconfigs:
        raise bb.parse.SkipRecipe("Required multiconfig %s is not available.  Either add the multiconfig to your configuration or adjust FW_TARGET:pn-%s." % (fw_target, d.getVar('PN')))
}
