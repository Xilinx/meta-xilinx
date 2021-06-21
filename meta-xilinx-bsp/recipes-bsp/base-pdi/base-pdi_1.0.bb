DESCRIPTION = "Recipe to deploy base pdi"

LICENSE = "CLOSED"

PROVIDES = "virtual/base-pdi"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_versal = "versal"

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

do_compile[noexec] = "1"

PDI_PATH ?= ""
SRC_URI += "${@['file://'+d.getVar('PDI_PATH'),''][d.getVar('PDI_PATH') == '']}"

#base install will just take from PDI_PATH variable
#will need to bbappend to this in meta-xilinx-tools to use xsct to extract pdi from xsa and install
do_install() {

    if [ -f ${WORKDIR}/${PDI_PATH} ];then
        install -d ${D}/boot
        install -m 0644 ${WORKDIR}/${PDI_PATH} ${D}/boot/base-design.pdi
    else
        bbfatal "No base pdi supplied"
    fi
}
SYSROOT_DIRS += "/boot"

FILES_${PN} += "/boot/*"
