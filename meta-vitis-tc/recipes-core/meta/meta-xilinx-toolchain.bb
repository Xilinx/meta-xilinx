SUMMARY = "Meta package for building a Xilinx prebuilt installable toolchain"
LICENSE = "MIT"

FILESEXTRAPATHS:append = ":${VITIS_TC_PATH}/scripts"

SRC_URI += " \
    file://relocate-wrapper.py \
"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit populate_sdk

COMPATIBLE_HOST = "${HOST_SYS}"

# This is a bare minimum toolchain, so limit to only the basic host
# dependencies
HOST_DEPENDS = " \
  nativesdk-sdk-provides-dummy \
"

S = "${UNPACKDIR}"

PLNX_ADD_VAI_SDK = ""

TOOLCHAIN_HOST_TASK = "${HOST_DEPENDS} packagegroup-cross-canadian-${MACHINE}"
TOOLCHAIN_TARGET_TASK:xilinx-standalone:baremetal-multilib-tc = "${@multilib_pkg_extend(d, 'packagegroup-newlib-standalone-sdk-target')}"

TOOLCHAIN_SHAR_EXT_TMPL = "${VITIS_TC_PATH}/files/toolchain-shar-extract.sh"
TOOLCHAIN_SHAR_REL_TMPL = "${VITIS_TC_PATH}/files/toolchain-shar-relocate.sh"

create_sdk_files:append () {
        cp ${S}/relocate-wrapper.py ${SDK_OUTPUT}/${SDKPATH}/
}

# The wrappers don't do anything, remove them!
create_sdk_files:append:sdkmingw32 () {
        rm -f ${SDK_OUTPUT}/${SDKPATH}/relocate-wrapper.py
        rm -f ${SDK_OUTPUT}/${SDKPATH}/relocate_sdk.py
        rm -f ${SDK_OUTPUT}/${SDKPATH}/post-relocate-setup.sh
}
