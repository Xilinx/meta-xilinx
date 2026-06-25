# Image types class to generate the image-recovery bin file

# Inherit bootgen-bif for BOOTGEN_ARCH and BIF generation functions
inherit bootgen-bif
IMAGE_TYPES += "imagercvry"

include recipes-bsp/bootbin/machine-xilinx-${SOC_FAMILY}.inc
# Delete the task do_extract_cdo to disable building boot.bin
deltask do_extract_cdo

IMGRCVRY_ATTR ?= "${BIF_PARTITION_ATTR}"
IMGRCVRY_KERNEL_ATTR ?= "linux-xlnx-imgrcvry rootfs"

IMGRCVRY_KERNEL_ADDR_DEFAULT = "0x200000"
IMGRCVRY_KERNEL_ADDR_DEFAULT:versal-2ve-2vm = "0x20200000"
IMGRCVRY_KERNEL_ADDR ?= "${IMGRCVRY_KERNEL_ADDR_DEFAULT}"

IMGRCVRY_ROOTFS_ADDR_DEFAULT = "0x4000000"
IMGRCVRY_ROOTFS_ADDR_DEFAULT:versal-2ve-2vm = "0x24000000"
IMGRCVRY_ROOTFS_ADDR ?= "${IMGRCVRY_ROOTFS_ADDR_DEFAULT}"

# specify BIF partition attributes for u-boot-xlnx-imgrcvry
BIF_SSBL_ATTR = "u-boot-xlnx-imgrcvry"
BIF_PARTITION_ATTR[u-boot-xlnx-imgrcvry] ?= "${@d.getVarFlag('BIF_PARTITION_ATTR', 'u-boot-xlnx', '')}"
BIF_PARTITION_IMAGE[u-boot-xlnx-imgrcvry] ?= "${@d.getVarFlag('BIF_PARTITION_IMAGE', 'u-boot-xlnx', '')}"
BIF_PARTITION_ID[u-boot-xlnx-imgrcvry] ?= "${@d.getVarFlag('BIF_PARTITION_ID', 'u-boot-xlnx', '')}"

# specify BIF partition attributes for linux-xlnx-imgrcvry
BIF_PARTITION_ATTR[linux-xlnx-imgrcvry] ?= "type=raw, load=${IMGRCVRY_KERNEL_ADDR}"
BIF_PARTITION_IMAGE[linux-xlnx-imgrcvry] ?= "${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}"
BIF_PARTITION_ID[linux-xlnx-imgrcvry] ?= "0x1c000000"

# specify BIF partition attributes for tiny-rootfs
# Allow the type to be overwritten easily
IMAGE_TYPEDEP:imagercvry ?= "cpio.gz.u-boot"

BIF_PARTITION_ATTR[rootfs] ?= "type=raw, load=${IMGRCVRY_ROOTFS_ADDR}"
BIF_PARTITION_IMAGE[rootfs] ?= "${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.${IMAGE_TYPEDEP:imagercvry}"
BIF_PARTITION_ID[rootfs] ?= "0x1c000000"

# BIF file path - set BIF_FILE_PATH so bootgen_bif_generate() uses it as default
IMGRCVRY_BIFFILE ?= "${B}/imgrcvry.bif"
BIF_FILE_PATH = "${IMGRCVRY_BIFFILE}"

IMGRCVRY_VERSION ?= "${DISTRO_VERSION}"
IMGRCVRY_VERFILE ?= "${B}/imgrcvry-version.txt"
IMGRCVRY_VERSION_STRING ?= "${DISTRO}-${MACHINE}-imgrcvry-v${IMGRCVRY_VERSION}"

IMGRCVRY_OPTIONAL_DATA_DEFAULT = ""
IMGRCVRY_OPTIONAL_DATA_DEFAULT:versal = "${IMGRCVRY_VERFILE}, id=0x21;"
IMGRCVRY_OPTIONAL_DATA_DEFAULT:versal-2ve-2vm = "${IMGRCVRY_VERFILE}, id=0x21;"
# FIXME: versal-net override missing (pre-existing)
IMGRCVRY_OPTIONAL_DATA ?= "${IMGRCVRY_OPTIONAL_DATA_DEFAULT}"

def write_imgrcvry_version(d):
    version_string = d.getVar('IMGRCVRY_VERSION_STRING')
    with open(d.expand(d.getVar('IMGRCVRY_VERFILE')), 'w') as f:
        f.write(version_string)

python do_imgrcvry_bif () {
    partitions = (d.getVar("IMGRCVRY_ATTR") or "").split()
    partitions += (d.getVar("IMGRCVRY_KERNEL_ATTR") or "").split()

    soc_family = d.getVar('SOC_FAMILY')
    if soc_family in ('versal', 'versal-2ve-2vm'):
        write_imgrcvry_version(d)

    optional_data = d.getVar("IMGRCVRY_OPTIONAL_DATA") or ""

    # Generate BIF, skip file check for rootfs (built later by IMAGE_TYPEDEP)
    bootgen_bif_generate(d,
        partitions=partitions,
        optional_data=optional_data,
        skip_check=['rootfs']
    )
}


IMAGE_CMD:imagercvry () {
	cd ${B}
	bootgen -image ${BIF_FILE_PATH} -arch ${BOOTGEN_ARCH} -w -o ${IMGDEPLOYDIR}/${IMAGE_NAME}.imgrcry.bin
	cd ${IMGDEPLOYDIR}
	if [ -e ${IMAGE_NAME}.imgrcry.bin ]; then
		ln -sf ${IMAGE_NAME}.imgrcry.bin ${IMAGE_LINK_NAME}.imgrcry.bin
	fi
}

addtask do_imgrcvry_bif after do_image before do_image_imagercvry

do_imgrcvry_bif[vardeps] += "\
    IMGRCVRY_OPTIONAL_DATA \
    IMGRCVRY_ATTR \
    IMGRCVRY_KERNEL_ATTR \
    BIF_FILE_PATH \
    BIF_PARTITION_ATTR \
    BIF_PARTITION_IMAGE \
    BIF_PARTITION_ID \
    BIF_PARTITION_NAME \
    BIF_COMMON_ATTR \
    SOC_FAMILY \
"

IMGRCVRY_ATTR_DEP = "${@(d.getVar('IMGRCVRY_ATTR') or "").replace('arm-trusted-firmware', 'virtual/arm-trusted-firmware').replace('bitstream', 'virtual/bitstream')}"
do_imgrcvry_bif[depends] += "${@' '.join('%s:do_populate_sysroot' % r for r in d.getVar('IMGRCVRY_ATTR_DEP').split())}"
do_imgrcvry_bif[depends] += "bootgen-native:do_populate_sysroot virtual/kernel:do_deploy"
