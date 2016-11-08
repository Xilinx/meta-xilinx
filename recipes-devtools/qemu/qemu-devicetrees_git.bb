SUMMARY = "Xilinx's hardware device trees required for QEMU"
HOMEPAGE = "https://github.com/xilinx/qemu-devicetrees/"
LICENSE = "BSD"
DEPENDS += "dtc-native"

inherit deploy

LIC_FILES_CHKSUM = "file://README;md5=0f52b512c21e3541b90ca9fa39aff8fe"

# Xilinx release 2016.4
SRCREV = "2311b00a5a8ebbde9fe5be542f654981110064d0"
SRC_URI = "git://github.com/Xilinx/qemu-devicetrees.git;protocol=https;nobranch=1"

S = "${WORKDIR}/git"

FILES_${PN} += " \
               LATEST/SINGLE_ARCH/* \
               LATEST/MULTI_ARCH/* \
               "

do_install[noexec] = "1"

do_deploy() {
       install -d ${DEPLOY_DIR_IMAGE}/qemu-hw-devicetrees

       for DTS_FILE in ${S}/LATEST/SINGLE_ARCH/*.dtb; do
               DTS_NAME=`basename -s .dtb ${DTS_FILE}`
               install -m 0644 ${S}/LATEST/SINGLE_ARCH/${DTS_NAME}.dtb \
                     ${DEPLOY_DIR_IMAGE}/qemu-hw-devicetrees/${DTS_NAME}-single.dtb
       done

       for DTS_FILE in ${S}/LATEST/MULTI_ARCH/*.dtb; do
               DTS_NAME=`basename -s .dtb ${DTS_FILE}`
               install -m 0644 ${S}/LATEST/MULTI_ARCH/${DTS_NAME}.dtb \
                            ${DEPLOY_DIR_IMAGE}/qemu-hw-devicetrees/${DTS_NAME}-multi.dtb
       done
}

addtask deploy after do_compile
