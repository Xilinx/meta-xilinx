SUMMARY = "Generates boot.bin using bootgen tool"
DESCRIPTION = "Manages task dependencies and creation of boot.bin. Use the \
BIF_PARTITION_xyz global variables and flags to determine what makes it into \
the image."

LICENSE = "BSD"

include machine-xilinx-${SOC_FAMILY}.inc

BOOTBIN_INCLUDE ?= ""

inherit deploy

# Don't allow building for microblaze MACHINE
COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:zynq = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

PROVIDES = "virtual/boot-bin"

DEPENDS += "bootgen-native ${UBOOT_BOOT_SCRIPT}"

# There is no bitstream recipe, so really depend on virtual/bitstream
# We need to refer to virtual/arm-trusted-firmware and not arm-trusted-firmware as there may be multiple providers
DEPENDS += "${@(d.getVar('BIF_PARTITION_ATTR') or "").replace('bitstream', 'virtual/bitstream').replace('arm-trusted-firmware', 'virtual/arm-trusted-firmware')}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

BIF_FILE_PATH ?= "${B}/bootgen.bif"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI += "${@('file://' + d.getVar("BIF_FILE_PATH")) if d.getVar("BIF_FILE_PATH") != (d.getVar('B') + '/bootgen.bif') else ''}"

# bootgen command -arch option for different SOC architectures
# zynq7000   : zynq
# zynqmp     : zynqmp
# versal     : versal
# versal-net : versalnet
# versal-2ve-2vm : versal_2ve_2vm
BOOTGEN_ARCH_DEFAULT = "undefined"
BOOTGEN_ARCH_DEFAULT:zynq = "zynq"
BOOTGEN_ARCH_DEFAULT:zynqmp = "zynqmp"
BOOTGEN_ARCH_DEFAULT:versal = "versal"
BOOTGEN_ARCH_DEFAULT:versal-net = "versalnet"
BOOTGEN_ARCH_DEFAULT:versal-2ve-2vm = "versal_2ve_2vm"
BOOTGEN_ARCH ?= "${BOOTGEN_ARCH_DEFAULT}"
BOOTGEN_EXTRA_ARGS ?= ""

QEMU_FLASH_TYPE_DEFAULT = "undefined"
QEMU_FLASH_TYPE_DEFAULT:zynq = "qspi"
QEMU_FLASH_TYPE_DEFAULT:zynqmp = "qspi"
QEMU_FLASH_TYPE_DEFAULT:versal = "${@'ospi' if d.getVar("QEMU_HW_BOOT_MODE") == '8' else 'qspi'}"
QEMU_FLASH_TYPE_DEFAULT:versal-net = "${@'ospi' if d.getVar("QEMU_HW_BOOT_MODE") == '8' else 'qspi'}"
QEMU_FLASH_TYPE_DEFAULT:versal-2ve-2vm = "${@'ospi' if d.getVar("QEMU_HW_BOOT_MODE") == '8' else 'qspi'}"
QEMU_FLASH_TYPE ?= "${QEMU_FLASH_TYPE_DEFAULT}"

BOOTSCR_DEP = ''
BOOTSCR_DEP:versal = '${UBOOT_BOOT_SCRIPT}:do_deploy'
BOOTSCR_DEP:versal-net = '${UBOOT_BOOT_SCRIPT}:do_deploy'
BOOTSCR_DEP:versal-2ve-2vm = '${UBOOT_BOOT_SCRIPT}:do_deploy'

BIF_BITSTREAM_ATTR ?= "${@bb.utils.contains('MACHINE_FEATURES', 'fpga-overlay', '', 'bitstream', d)}"

do_patch[noexec] = "1"

do_compile[depends] .= " ${BOOTSCR_DEP}"

def create_bif(config, attrflags, attrimage, ids, common_attr, biffd, d):
    arch = d.getVar("SOC_FAMILY")
    bb.error("create_bif function not defined for arch: %s" % (arch))

def create_zynq_bif(config, attrflags, attrimage, ids, common_attr, biffd, d):
    import re, os
    for cfg in config:
        if cfg not in attrflags and common_attr:
            error_msg = "%s: invalid ATTRIBUTE" % (cfg)
            bb.error("BIF attribute Error: %s " % (error_msg))
        else:
            if common_attr:
                cfgval = d.expand(attrflags[cfg]).split(',')
                cfgstr = "\t [%s] %s\n" % (cfg,', '.join(cfgval))
            else:
                if cfg not in attrimage:
                    error_msg = "%s: invalid or missing elf or image" % (cfg)
                    bb.error("BIF atrribute Error: %s " % (error_msg))
                imagestr = d.expand(attrimage[cfg])
                if not os.path.exists(imagestr):
                    bb.fatal("Expected file %s, specified from the bif file does not exists!" %(imagestr))
                if os.stat(imagestr).st_size == 0:
                    bb.warn("Empty file %s, excluding from bif file" %(imagestr))
                    continue
                if cfg in attrflags:
                    cfgval = d.expand(attrflags[cfg]).split(',')
                    cfgstr = "\t [%s] %s\n" % (', '.join(cfgval), imagestr)
                else:
                    cfgstr = "\t %s\n" % (imagestr)
            biffd.write(cfgstr)

    return

def create_versal_bif(config, attrflags, attrimage, ids, common_attr, biffd, d):
    import re, os
    id_dict = {}
    for cfg in config:
        if cfg not in attrflags and common_attr:
            error_msg = "%s: invalid ATTRIBUTE" % (cfg)
            bb.error("BIF attribute Error: %s " % (error_msg))
        else:
            if common_attr:
                cfgval = d.expand(attrflags[cfg]).split(',')
                #TODO: Does common attribute syntax change in similar way for versal?
                cfgstr = "\t { %s %s }\n" % (cfg,', '.join(cfgval))
                biffd.write(cfgstr)
            else:
                if cfg not in attrimage:
                    error_msg = "%s: invalid or missing elf or image" % (cfg)
                    bb.error("BIF atrribute Error: %s " % (error_msg))
                imagestr = d.expand(attrimage[cfg])
                if os.stat(imagestr).st_size == 0:
                    bb.warn("Empty file %s, excluding from bif file" %(imagestr))
                    continue
                if cfg in attrflags:
                    cfgval = d.expand(attrflags[cfg]).split(',')
                    try:
                        id = d.expand(ids[cfg])
                    except:
                        id = '0'
                    cfgstr = "\t { %s, file=%s }\n" % (', '.join(cfgval), imagestr)
                    try:
                        id_dict[id] += cfgstr
                    except:
                        id_dict[id] = cfgstr
                else:
                    cfgstr = "\t %s\n" % (imagestr)
    for id, string in id_dict.items():
        biffd.write("\timage {\n")
        if id != '0':
            biffd.write("\t id = " + id + ", name=apu_ss\n")
        biffd.write(string)
        biffd.write("\t}\n")
    return

python do_configure() {
    import shutil

    fp = d.getVar("BIF_FILE_PATH")
    if fp == (d.getVar('B') + '/bootgen.bif'):
        biffd = open(fp, 'w')
        biffd.write("the_ROM_image:\n")
        biffd.write("{\n")

        for opt_data in (d.getVar("BIF_OPTIONAL_DATA") or "").split(';'):
            if opt_data:
                # Format per UG1283:
                # optionaldata {<filename>, id=<id>}
                try:
                    (fname, id) = opt_data.split(',')
                    fname = d.expand(fname)
                except:
                    bb.error('BIF_OPTIONAL_DATA value "%s" not specified properly, expected: <filename>, id=<id>' % opt_data)

                dest = os.path.join(d.getVar('B'), os.path.basename(fname))
                print('Copy BIF_OPTIONALDATA element %s -> %s' % (fname, dest))
                shutil.copyfile(fname, os.path.join(d.getVar('B'), os.path.basename(fname)))

                biffd.write("\toptionaldata { %s, %s }\n" % (os.path.basename(fname), id))

        # Common attributes are not allowed to point to files, the Partition attributes are used for that
        arch = d.getVar("SOC_FAMILY")
        bifattr = (d.getVar("BIF_COMMON_ATTR") or "").split()
        if bifattr:
            attrflags = d.getVarFlags("BIF_COMMON_ATTR") or {}
            if arch in ['zynq', 'zynqmp']:
                create_zynq_bif(bifattr, attrflags,'','', 1, biffd, d)
            elif arch in ['versal', 'versal-net', 'versal-2ve-2vm']:
                create_versal_bif(bifattr, attrflags,'','', 1, biffd, d)
            else:
                create_bif(bifattr, attrflags,'','', 1, biffd, d)

        # Partition Attributes are made up of Attribute and Image
        # Image needs to be copied and filename sanitized
        bifpartition = (d.getVar("BIF_PARTITION_ATTR") or "").split()
        if bifpartition:
            attrflags = d.getVarFlags("BIF_PARTITION_ATTR") or {}
            attrimage = d.getVarFlags("BIF_PARTITION_IMAGE") or {}
            ids = d.getVarFlags("BIF_PARTITION_ID") or {}

            local_attrimage = {}
            for part in bifpartition:
                try:
                    fname = d.expand(attrimage[part])
                except:
                    bb.error('BIF_PARTITION_ATTR[%s] not defined, but referenced in BIF_PARTITION_ATTR', part)

                dest = os.path.join(d.getVar('B'), os.path.basename(fname))
                print('Copy BIF_PARTITION_IMAGE[%s] %s -> %s' % (part, fname, dest))
                shutil.copyfile(fname, os.path.join(d.getVar('B'), os.path.basename(fname)))

                local_attrimage[part] = os.path.basename(fname)

            if arch in ['zynq', 'zynqmp']:
                create_zynq_bif(bifpartition, attrflags, local_attrimage, ids, 0, biffd, d)
            elif arch in ['versal', 'versal-net', 'versal-2ve-2vm']:
                create_versal_bif(bifpartition, attrflags, local_attrimage, ids, 0, biffd, d)
            else:
                create_bif(bifpartition, attrflags, local_attrimage, ids, 0, biffd, d)

        biffd.write("}")
        biffd.close()
    else:
        print("Using custom BIF file: " + d.getVar("BIF_FILE_PATH") )
}

do_configure[vardeps] += "BIF_PARTITION_ATTR BIF_PARTITION_IMAGE BIF_COMMON_ATTR"
do_configure[vardeps] += "BIF_FSBL_ATTR BIF_BITSTREAM_ATTR BIF_ATF_ATTR BIF_DEVICETREE_ATTR BIF_SSBL_ATTR"

do_compile() {
    rm -f ${B}/BOOT.bin
    if [ "${BIF_FILE_PATH}" != "${B}/bootgen.bif" ];then
        BIF_FILE_PATH="${WORKDIR}${BIF_FILE_PATH}"
    fi
    bootgen -image ${BIF_FILE_PATH} -arch ${BOOTGEN_ARCH} ${BOOTGEN_EXTRA_ARGS} -w -o ${B}/BOOT.bin
    if [ ! -e ${B}/BOOT.bin ]; then
        bbfatal "bootgen failed. See log"
    fi
}

do_compile:append:versal() {
    dd if=/dev/zero bs=256M count=1  > ${B}/qemu-${QEMU_FLASH_TYPE}.bin
    dd if=${B}/BOOT.bin of=${B}/qemu-${QEMU_FLASH_TYPE}.bin bs=1 seek=0 conv=notrunc
    dd if=${DEPLOY_DIR_IMAGE}/boot.scr of=${B}/qemu-${QEMU_FLASH_TYPE}.bin bs=1 seek=66584576 conv=notrunc
}

do_compile:append:versal-net() {
    dd if=/dev/zero bs=256M count=1  > ${B}/qemu-${QEMU_FLASH_TYPE}.bin
    dd if=${B}/BOOT.bin of=${B}/qemu-${QEMU_FLASH_TYPE}.bin bs=1 seek=0 conv=notrunc
    dd if=${DEPLOY_DIR_IMAGE}/boot.scr of=${B}/qemu-${QEMU_FLASH_TYPE}.bin bs=1 seek=66584576 conv=notrunc
}

do_compile:append:versal-2ve-2vm() {
    dd if=/dev/zero bs=256M count=1  > ${B}/qemu-${QEMU_FLASH_TYPE}.bin
    dd if=${B}/BOOT.bin of=${B}/qemu-${QEMU_FLASH_TYPE}.bin bs=1 seek=0 conv=notrunc
    dd if=${DEPLOY_DIR_IMAGE}/boot.scr of=${B}/qemu-${QEMU_FLASH_TYPE}.bin bs=1 seek=66584576 conv=notrunc
}

do_install() {
    install -d ${D}/boot
    install -m 0644 ${B}/BOOT.bin ${D}/boot/BOOT.bin
}

inherit image-artifact-names

QEMU_FLASH_IMAGE_NAME ?= "qemu-${QEMU_FLASH_TYPE}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

BOOTBIN_LINK_NAME ?= "BOOT-${MACHINE}"
BOOTBIN_BASE_NAME ?= "BOOT-${MACHINE}${IMAGE_VERSION_SUFFIX}"

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${B}/BOOT.bin ${DEPLOYDIR}/${BOOTBIN_BASE_NAME}.bin
    ln -sf ${BOOTBIN_BASE_NAME}.bin ${DEPLOYDIR}/${BOOTBIN_LINK_NAME}.bin
    ln -sf ${BOOTBIN_BASE_NAME}.bin ${DEPLOYDIR}/boot.bin

    install -d ${DEPLOYDIR}/boot.bin-extracted
    install -m 0644 ${B}/* ${DEPLOYDIR}/boot.bin-extracted/.
    rm -f ${DEPLOYDIR}/boot.bin-extracted/BOOT.bin
}

do_deploy:append:versal () {

    install -m 0644 ${B}/BOOT_bh.bin ${DEPLOYDIR}/${BOOTBIN_BASE_NAME}_bh.bin
    ln -sf ${BOOTBIN_BASE_NAME}_bh.bin ${DEPLOYDIR}/BOOT-${MACHINE}_bh.bin

    install -m 0644 ${B}/qemu-${QEMU_FLASH_TYPE}.bin ${DEPLOYDIR}/${QEMU_FLASH_IMAGE_NAME}.bin
    ln -sf ${QEMU_FLASH_IMAGE_NAME}.bin ${DEPLOYDIR}/qemu-${QEMU_FLASH_TYPE}-${MACHINE}.bin
}

do_deploy:append:versal-net () {
    install -m 0644 ${B}/BOOT_bh.bin ${DEPLOYDIR}/${BOOTBIN_BASE_NAME}_bh.bin
    ln -sf ${BOOTBIN_BASE_NAME}_bh.bin ${DEPLOYDIR}/BOOT-${MACHINE}_bh.bin

    install -m 0644 ${B}/qemu-${QEMU_FLASH_TYPE}.bin ${DEPLOYDIR}/${QEMU_FLASH_IMAGE_NAME}.bin
    ln -sf ${QEMU_FLASH_IMAGE_NAME}.bin ${DEPLOYDIR}/qemu-${QEMU_FLASH_TYPE}-${MACHINE}.bin

}

do_deploy:append:versal-2ve-2vm() {

    install -m 0644 ${B}/BOOT_bh.bin ${DEPLOYDIR}/${BOOTBIN_BASE_NAME}_bh.bin
    ln -sf ${BOOTBIN_BASE_NAME}_bh.bin ${DEPLOYDIR}/BOOT-${MACHINE}_bh.bin

    install -m 0644 ${B}/qemu-${QEMU_FLASH_TYPE}.bin ${DEPLOYDIR}/${QEMU_FLASH_IMAGE_NAME}.bin
    ln -sf ${QEMU_FLASH_IMAGE_NAME}.bin ${DEPLOYDIR}/qemu-${QEMU_FLASH_TYPE}-${MACHINE}.bin
}

FILES:${PN} += "/boot/BOOT.bin"
SYSROOT_DIRS += "/boot"

addtask do_deploy before do_build after do_compile

# We want to deploy this into the build directory and copy it later
IMGDEPLOYDIR ??= "${DEPLOYDIR}"
IMAGE_LINK_NAME = "${BOOTBIN_LINK_NAME}"
IMAGE_NAME = "${BOOTBIN_BASE_NAME}"

inherit ${@bb.utils.contains('IMAGE_CLASSES', 'qemuboot-xilinx', 'qemuboot-xilinx', '', d)}
do_deploy[postfuncs] += "${@bb.utils.contains('IMAGE_CLASSES', 'qemuboot-xilinx', 'do_write_qemuboot_conf', '', d)}"
