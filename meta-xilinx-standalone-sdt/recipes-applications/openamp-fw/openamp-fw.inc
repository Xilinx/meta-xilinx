SUMMARY = "Sample recipe to package and deploy baremetal or freertos elf or bin to linux rootfs"
LICENSE = "CLOSED"

inherit check_sdt_enabled fw-package

OPENAMP_XLNX_RECIPE ?= "open-amp-xlnx-echo"

FW_OS ?= "${MCNAME}"

TARGET_MC:zynqmp = "cortexr5-0-${FW_OS}"
TARGET_MC:versal = "cortexr5-0-${FW_OS}"
TARGET_MC:versal-net = "cortexr52-0-${FW_OS}"
FW_MCDEPENDS = "mc::${MACHINE}-${TARGET_MC}:${OPENAMP_XLNX_RECIPE}-${FW_OS}:do_deploy"
FW_DEPLOY_DIR ?= "${TMPDIR}-${MACHINE}-${TARGET_MC}/deploy/images/${MACHINE}"

DESTDIR = "${nonarch_base_libdir}/firmware"

OPENAMP_FW_NAME ?= "image_echo_test"

OPENAMP_FW_SRC_NAME ?= "${OPENAMP_XLNX_RECIPE}-${FW_OS}.elf"
FW_NAME ?= "${OPENAMP_FW_SRC_NAME}"
FW_FILE ?= "${FW_DEPLOY_DIR}/${OPENAMP_FW_SRC_NAME}"
FW_IMAGE_NAME ?= "${OPENAMP_FW_NAME}"

DEPENDS = " open-amp-xlnx "

do_install() {
    if [ ! -e ${FW_DEPLOY_DIR}/${OPENAMP_FW_SRC_NAME} ]; then
        echo "Unable to find FW_FILE (${OPENAMP_FW_SRC_NAME})"
        exit 1
    fi

    install -Dm 0644 ${FW_DEPLOY_DIR}/${OPENAMP_FW_SRC_NAME} ${D}${DESTDIR}/${FW_IMAGE_NAME}
}

# If the item is already in OUR deploy_image_dir, nothing to deploy!
SHOULD_DEPLOY = "${@'false' if (d.getVar('FW_FILE')).startswith(d.getVar('DEPLOY_DIR_IMAGE')) else 'true'}"
do_deploy() {
    # If the item is already in OUR deploy_image_dir, nothing to deploy!
    if ${SHOULD_DEPLOY}; then
        install -Dm 0644 ${FW_FILE} ${DEPLOYDIR}/${FW_IMAGE_NAME}
    fi
}

SUPPORTED_OS_LIST = "baremetal freertos"
BBCLASSEXTEND = "${@' '.join(['mcextend:'+x for x in d.getVar('SUPPORTED_OS_LIST').split()])}"
python () {
    if not d.getVar("MCNAME"):
        raise bb.parse.SkipRecipe("No class extension set, %s is supported" % d.getVar('SUPPORTED_OS_LIST'))
    else:
        mc_dep = d.getVar("FW_MCDEPENDS")
        if mc_dep:
            mc = mc_dep.split(':')[2]
            if not mc in (d.getVar('BBMULTICONFIG') or ""):
                d.setVar('BB_DONT_CACHE', '1')
                raise bb.parse.SkipRecipe("Requires multiconfig %s, which is not available." % d.getVar(mc))
}
