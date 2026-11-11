SUMMARY = "Image Store PDI holding the FPD CDO"
DESCRIPTION = "Wraps the FPD CDO in a partial PDI so it can be placed in the \
Image Store region of a boot image, letting PLM reload it from reserved DDR \
instead of the boot device."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit bootgen-bif

# The activating BSP opts its machine in; it also supplies id_code below.
COMPATIBLE_MACHINE = "^$"

DEPENDS += "bootgen-native"

PACKAGE_ARCH ?= "${MACHINE_ARCH}"

FPD_DATA_CDO ?= "${DEPLOY_DIR_IMAGE}/CDO/fpd_data.cdo"

BIF_FILE_PATH = "${B}/${PN}.bif"

# A standalone PDI states its own header. id_code identifies the device, so the
# BSP supplies it; extended_id_code is invariant at 0x01 on this family.
BIF_TOPLEVEL_ATTR = "id_code extended_id_code id"
BIF_TOPLEVEL_ATTR[extended_id_code] = "0x01"
BIF_TOPLEVEL_ATTR[id] = "0x2"

BIF_PARTITION_ATTR = "fpd-cdo"
BIF_PARTITION_ATTR[fpd-cdo] = "id = 0x08, type = cdo"
BIF_PARTITION_IMAGE[fpd-cdo] = "${FPD_DATA_CDO}"
BIF_PARTITION_ID[fpd-cdo] = "0x420c003"
BIF_PARTITION_NAME[0x420c003] = "fpd_image"

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

python do_generate_bif() {
    bootgen_bif_generate(d)
}
do_generate_bif[dirs] = "${B}"
do_generate_bif[depends] += "cdo-deploy:do_deploy"
do_generate_bif[vardeps] += "BIF_FILE_PATH BIF_TOPLEVEL_ATTR BIF_PARTITION_ATTR \
                             BIF_PARTITION_ID BIF_PARTITION_IMAGE BIF_PARTITION_NAME"
addtask generate_bif after do_configure before do_bootgen

do_bootgen[dirs] = "${B}"
do_bootgen() {
    rm -f ${B}/${PN}.pdi
    bootgen -image ${BIF_FILE_PATH} -arch ${BOOTGEN_ARCH} -w -o ${B}/${PN}.pdi
    if [ ! -e ${B}/${PN}.pdi ]; then
        bbfatal "bootgen failed. See log"
    fi
}
addtask bootgen after do_generate_bif before do_install

# In the sysroot like the other partition images, so DEPENDS alone orders it
do_install() {
    install -Dm 0644 ${B}/${PN}.pdi ${D}/firmware/${PN}.pdi
}

SYSROOT_DIRS += "/firmware"
FILES:${PN} += "/firmware/${PN}.pdi"
