DESCRIPTION = "Recipe to deploy base pdi"

LICENSE = "CLOSED"

PROVIDES = "virtual/base-pdi"

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:microblaze = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

do_compile[noexec] = "1"

PDI_PATH ?= ""
SRC_URI += "${@['file://'+d.getVar('PDI_PATH'),''][d.getVar('PDI_PATH') == '']}"

python() {
    if d.getVar('PDI_SKIP_CHECK') != "1" and not d.getVar('PDI_PATH'):
        raise bb.parse.SkipRecipe("PDI_PATH is not configured with the base design pdi")
}

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

FILES:${PN} += "/boot/*"
