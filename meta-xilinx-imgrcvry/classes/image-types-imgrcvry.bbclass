# Image types class to generate the image-recovery bin file

IMAGE_TYPES += "imagercvry"

include recipes-bsp/bootbin/machine-xilinx-${SOC_FAMILY}.inc

IMGRCVRY_ATTR ?= "${BIF_PARTITION_ATTR}"
IMGRCVRY_KERNEL_ATTR ?= "linux-xlnx rootfs"

IMGRCVRY_KERNEL_ADDR_DEFAULT = "0x200000"
IMGRCVRY_KERNEL_ADDR_DEFAULT:versal-2ve-2vm = "0x20200000"
IMGRCVRY_KERNEL_ADDR ?= "${IMGRCVRY_KERNEL_ADDR_DEFAULT}"

IMGRCVRY_ROOTFS_ADDR_DEFAULT = "0x4000000"
IMGRCVRY_ROOTFS_ADDR_DEFAULT:versal-2ve-2vm = "0x24000000"
IMGRCVRY_ROOTFS_ADDR ?= "${IMGRCVRY_ROOTFS_ADDR_DEFAULT}"

# specify BIF partition attributes for linux-xlnx
BIF_PARTITION_ATTR[linux-xlnx] ?= "type=raw, load=${IMGRCVRY_KERNEL_ADDR}"
BIF_PARTITION_IMAGE[linux-xlnx] ?= "${DEPLOY_DIR_IMAGE}/Image.gz"
BIF_PARTITION_ID[linux-xlnx] ?= "0x1c000000"

# specify BIF partition attributes for tiny-rootfs
BIF_PARTITION_ATTR[rootfs] ?= "type=raw, load=${IMGRCVRY_ROOTFS_ADDR}"
BIF_PARTITION_IMAGE[rootfs] ?= "${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.cpio.gz.u-boot"
BIF_PARTITION_ID[rootfs] ?= "0x1c000000"

BOOTGEN_ARCH_DEFAULT = ""
BOOTGEN_ARCH_DEFAULT:zynqmp = "zynqmp"
BOOTGEN_ARCH_DEFAULT:versal = "versal"
BOOTGEN_ARCH_DEFAULT:versal-2ve-2vm = "versal_2ve_2vm"
BOOTGEN_ARCH ?= "${BOOTGEN_ARCH_DEFAULT}"

IMGRCVRY_BIFFILE ?= "${B}/imgrcvry.bif"

IMGRCVRY_VERSION ?= "${DISTRO_VERSION}"
IMGRCVRY_VERFILE ?= "${B}/imgrcvry-version.txt"
IMGRCVRY_VERSION_STRING ?= "${DISTRO}-${MACHINE}-imgrcvry-v${IMGRCVRY_VERSION}"

IMGRCVRY_OPTIONAL_DATA_DEFAULT = ""
IMGRCVRY_OPTIONAL_DATA_DEFAULT:versal = "${IMGRCVRY_VERFILE}, id=0x21;"
IMGRCVRY_OPTIONAL_DATA_DEFAULT:versal-2ve-2vm = "${IMGRCVRY_VERFILE}, id=0x21;"
IMGRCVRY_OPTIONAL_DATA ?= "${IMGRCVRY_OPTIONAL_DATA_DEFAULT}"

def write_imgrcvry_version(d):
    version_string = d.getVar('IMGRCVRY_VERSION_STRING')
    with open(d.expand(d.getVar('IMGRCVRY_VERFILE')), 'w') as f:
        f.write(version_string)

def write_zynqmp_bif(bifpartition, attrflags, attrimage, ids, d):
    biffile_str = 'the_imgrcvry_image:\n'
    biffile_str += '{\n'
    import os
    for attr in bifpartition:
        attr_path = d.expand(attrimage[attr])
        attr_flag = d.expand(attrflags[attr])
        # IMAGE_TYPEDEP will run after the do_imgrcvry_bif so skip rootfs file check
        if not os.path.exists(attr_path) and attr != 'rootfs':
            bb.fatal("Expected file %s, specified from the bif file does not exists!" %(attr_path))
        if attr in attrflags:
            biffile_str += "\t [%s] %s\n" % (attr_flag, attr_path)
        else:
            biffile_str += "\t %s\n" % (attr_path)
    biffile_str += '}\n'

    return biffile_str


def write_versal_bif(bifpartition, attrflags, attrimage, ids, d):
    idcode_dict = {}
    import os
    for attr in bifpartition:
        attr_path = d.expand(attrimage[attr])
        attr_flags = d.expand(attrflags[attr])
        # IMAGE_TYPEDEP will run after the do_imgrcvry_bif so skip rootfs file check
        if not os.path.exists(attr_path) and attr != 'rootfs':
            bb.fatal("Expected file %s, specified from the bif file does not exists!" %(attr_path))
        if attr and attr_flags:
            bifstr = "\t { %s, file=%s }\n" % (attr_flags, attr_path)
            try:
                id = d.expand(ids[attr])
            except:
                id = '0'
            try:
                idcode_dict[id] += bifstr
            except:
                idcode_dict[id] = bifstr

    biffile_str = 'the_imgrcvry_image:\n'
    biffile_str += '{\n'
    for opt_data in (d.getVar("IMGRCVRY_OPTIONAL_DATA") or "").split(';'):
        if not opt_data:
            continue
        try:
            (fname, id) = opt_data.split(',')
            fname = d.expand(fname)
        except:
            bb.error('IMGRCVRY_OPTIONAL_DATA value "%s" not specified properly, expected: <filename>, id=<id>' % opt_data)
        if not os.path.exists(fname):
            bb.warn('IMGRCVRY_OPTIONAL_DATA specified file doesnot exists: %s' % fname)
        biffile_str += '\toptionaldata { %s, %s }\n' % (fname, id)

    for id, string in idcode_dict.items():
        biffile_str += '\timage {\n'
        if id != '0':
            biffile_str += '\t id = ' + id + '\n'
        biffile_str += string
        biffile_str += '\t}\n'
    biffile_str += '}\n'

    return biffile_str


python do_imgrcvry_bif () {
    bifpartition = (d.getVar("IMGRCVRY_ATTR") or "").split()
    bifpartition += (d.getVar("IMGRCVRY_KERNEL_ATTR") or "").split()
    attrflags = d.getVarFlags("BIF_PARTITION_ATTR") or {}
    attrimage = d.getVarFlags("BIF_PARTITION_IMAGE") or {}
    ids = d.getVarFlags("BIF_PARTITION_ID") or {}
   
    soc_family = d.getVar('SOC_FAMILY')
    biffile_str = ''
    if soc_family in ('versal', 'versal-2ve-2vm'):
        write_imgrcvry_version(d)
        biffile_str = write_versal_bif(bifpartition, attrflags, attrimage, ids, d)
    elif soc_family in ('zynqmp'):
        biffile_str = write_zynqmp_bif(bifpartition, attrflags, attrimage, ids, d)
    else:
        pass
    with open(d.getVar('IMGRCVRY_BIFFILE'), 'w') as f:
        f.write(biffile_str)
    f.close()
}


IMAGE_CMD:imagercvry () {
	bootgen -image ${IMGRCVRY_BIFFILE} -arch ${BOOTGEN_ARCH} -w -o ${IMGDEPLOYDIR}/${IMAGE_NAME}.imgrcry.bin
	cd ${IMGDEPLOYDIR}
	if [ -e ${IMAGE_NAME}.imgrcry.bin ]; then
		ln -sf ${IMAGE_NAME}.imgrcry.bin ${IMAGE_LINK_NAME}.imgrcry.bin
	fi
}

addtask do_imgrcvry_bif after do_image before do_image_imagercvry

do_imgrcvry_bif[vardeps] += "IMGRCVRY_OPTIONAL_DATA IMGRCVRY_ATTR IMGRCVRY_BIFFILE IMGRCVRY_KERNEL_ATTR BIF_PARTITION_IMAGE BIF_COMMON_ATTR BIF_PARTITION_ID"

IMGRCVRY_ATTR_DEP = "${@(d.getVar('IMGRCVRY_ATTR') or "").replace('arm-trusted-firmware', 'virtual/arm-trusted-firmware').replace('bitstream', 'virtual/bitstream')}"
do_imgrcvry_bif[depends] += "${@' '.join('%s:do_populate_sysroot' % r for r in d.getVar('IMGRCVRY_ATTR_DEP').split())}"
do_imgrcvry_bif[depends] += "bootgen-native:do_populate_sysroot virtual/kernel:do_deploy"

IMAGE_TYPEDEP:imagercvry = "cpio.gz.u-boot"
