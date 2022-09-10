require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native bison-native xxd-native"

XILINX_RELEASE_VERSION ?= ""
UBOOT_VERSION_EXTENSION ?= "-xilinx-${XILINX_RELEASE_VERSION}"
PV = "${UBOOT_VERSION}${UBOOT_VERSION_EXTENSION}+git${SRCPV}"

UBOOTURI ?= "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https"
UBRANCH ?= ""
UBRANCHARG = "${@['nobranch=1', 'branch=${UBRANCH}'][d.getVar('UBRANCH', True) != '']}"

SRC_URI = "${UBOOTURI};${UBRANCHARG}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

FILESEXTRAPATHS:prepend := "${THISDIR}/u-boot:"

SYSROOT_DIRS += "/boot"

BASE_DTS ?= "${@os.path.basename(d.getVar('CONFIG_DTFILE') or '').rstrip('.dtb') or 'system-top'}"
DTB_PATH ?= "/boot/devicetree/"
DTB_NAME ?= ""

EXTRA_OEMAKE += "${@'EXT_DTB=${RECIPE_SYSROOT}/${DTB_PATH}/${DTB_NAME}' if (d.getVar('DTB_NAME') != '') else '' }"

python __anonymous () {
    #check if there are any dtb providers
    providerdtb = d.getVar("PREFERRED_PROVIDER_virtual/dtb")
    if providerdtb:
       d.appendVarFlag('do_configure', 'depends', ' virtual/dtb:do_populate_sysroot')
       if d.getVar("DTB_NAME") is not None:
           d.setVar('DTB_NAME', d.getVar('BASE_DTS')+ '.dtb')
}

require u-boot-xlnx-blob.inc

UBOOTELF_NODTB_IMAGE ?= "u-boot-nodtb.elf"
UBOOTELF_NODTB_BINARY ?= "u-boot"
do_deploy:prepend() {
    cd ${B}

    if [ -f "${UBOOTELF_NODTB_BINARY}" ]; then
            install ${UBOOTELF_NODTB_BINARY} ${DEPLOYDIR}/${UBOOTELF_NODTB_IMAGE}
    fi

    #following lines are from uboot-sign.bbclass, vars are defined there
    if [ -e "${UBOOT_DTB_BINARY}" ]; then
            install ${UBOOT_DTB_BINARY} ${DEPLOYDIR}/${UBOOT_DTB_IMAGE}
            ln -sf ${UBOOT_DTB_IMAGE} ${DEPLOYDIR}/${UBOOT_DTB_BINARY}
            ln -sf ${UBOOT_DTB_IMAGE} ${DEPLOYDIR}/${UBOOT_DTB_SYMLINK}
    fi
    if [ -f "${UBOOT_NODTB_BINARY}" ]; then
            install ${UBOOT_NODTB_BINARY} ${DEPLOYDIR}/${UBOOT_NODTB_IMAGE}
            ln -sf ${UBOOT_NODTB_IMAGE} ${DEPLOYDIR}/${UBOOT_NODTB_SYMLINK}
            ln -sf ${UBOOT_NODTB_IMAGE} ${DEPLOYDIR}/${UBOOT_NODTB_BINARY}
    fi
}
