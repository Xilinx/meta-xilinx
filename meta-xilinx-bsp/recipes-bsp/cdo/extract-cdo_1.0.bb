DESCRIPTION = "Recipe to copy external cdos"

LICENSE = "CLOSED"

inherit deploy

PROVIDES = "virtual/cdo"

DEPENDS += "bootgen-native"

do_compile[depends] += "virtual/boot-bin:do_deploy"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_versal = "versal"

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

B = "${WORKDIR}/build"

BOOTGEN_CMD ?= "bootgen"
BOOTGEN_ARGS ?= "-arch versal"
BOOTGEN_OUTFILE ?= "${DEPLOY_DIR_IMAGE}/boot.bin"

#The following line creates the pmc_cdo.bin file at the same dir as the boot.bin which is DEPLOY_DIR_IMAGE
do_compile() {
    ${BOOTGEN_CMD} ${BOOTGEN_ARGS} -dump ${BOOTGEN_OUTFILE} pmc_cdo
}

do_deploy() {
    install -d ${DEPLOYDIR}/CDO
    install -m 0644 ${DEPLOY_DIR_IMAGE}/pmc_cdo.bin ${DEPLOYDIR}/CDO/pmc_cdo.bin
}

addtask do_deploy after do_install
