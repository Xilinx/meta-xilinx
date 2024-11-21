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

PROVIDES = "virtual/boot-bin"

DEPENDS += "bootgen-native u-boot-xlnx-scr"

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
BOOTGEN_ARCH_DEFAULT = "undefined"
BOOTGEN_ARCH_DEFAULT:zynq = "zynq"
BOOTGEN_ARCH_DEFAULT:zynqmp = "zynqmp"
BOOTGEN_ARCH_DEFAULT:versal = "versal"
BOOTGEN_ARCH_DEFAULT:versal-net = "versalnet"
BOOTGEN_ARCH ?= "${BOOTGEN_ARCH_DEFAULT}"
BOOTGEN_EXTRA_ARGS ?= ""

QEMU_FLASH_TYPE ?= "qspi"
BOOTSCR_DEP = ''
BOOTSCR_DEP:versal = 'u-boot-xlnx-scr:do_deploy'
BOOTSCR_DEP:versal-net = 'u-boot-xlnx-scr:do_deploy'

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
            biffd.write("\t id = " + id + "\n")
        biffd.write(string)
        biffd.write("\t}\n")
    return

python do_configure() {
    fp = d.getVar("BIF_FILE_PATH")
    if fp == (d.getVar('B') + '/bootgen.bif'):
        biffd = open(fp, 'w')
        biffd.write("the_ROM_image:\n")
        biffd.write("{\n")

        if d.getVar("BIF_OPTIONAL_DATA"):
            opt_data = d.getVar("BIF_OPTIONAL_DATA") or ""
            biffd.write("\toptionaldata { %s }\n" % opt_data)

        arch = d.getVar("SOC_FAMILY")
        bifattr = (d.getVar("BIF_COMMON_ATTR") or "").split()
        if bifattr:
            attrflags = d.getVarFlags("BIF_COMMON_ATTR") or {}
            if arch in ['zynq', 'zynqmp']:
                create_zynq_bif(bifattr, attrflags,'','', 1, biffd, d)
            elif arch in ['versal', 'versal-net']:
                create_versal_bif(bifattr, attrflags,'','', 1, biffd, d)
            else:
                create_bif(bifattr, attrflags,'','', 1, biffd, d)

        bifpartition = (d.getVar("BIF_PARTITION_ATTR") or "").split()
        if bifpartition:
            attrflags = d.getVarFlags("BIF_PARTITION_ATTR") or {}
            attrimage = d.getVarFlags("BIF_PARTITION_IMAGE") or {}
            ids = d.getVarFlags("BIF_PARTITION_ID") or {}
            if arch in ['zynq', 'zynqmp']:
                create_zynq_bif(bifpartition, attrflags, attrimage, ids, 0, biffd, d)
            elif arch in ['versal', 'versal-net']:
                create_versal_bif(bifpartition, attrflags, attrimage, ids, 0, biffd, d)
            else:
                create_bif(bifpartition, attrflags, attrimage, ids, 0, biffd, d)

        biffd.write("}")
        biffd.close()
    else:
        print("Using custom BIF file: " + d.getVar("BIF_FILE_PATH") )
}

do_configure[vardeps] += "BIF_PARTITION_ATTR BIF_PARTITION_IMAGE BIF_COMMON_ATTR"
do_configure[vardeps] += "BIF_FSBL_ATTR BIF_BITSTREAM_ATTR BIF_ATF_ATTR BIF_DEVICETREE_ATTR BIF_SSBL_ATTR"

do_compile() {
    cd ${WORKDIR}
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

do_install() {
    install -d ${D}/boot
    install -m 0644 ${B}/BOOT.bin ${D}/boot/BOOT.bin
}

inherit image-artifact-names

QEMU_FLASH_IMAGE_NAME ?= "qemu-${QEMU_FLASH_TYPE}-${MACHINE}${IMAGE_VERSION_SUFFIX}"

BOOTBIN_BASE_NAME ?= "BOOT-${MACHINE}${IMAGE_VERSION_SUFFIX}"

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${B}/BOOT.bin ${DEPLOYDIR}/${BOOTBIN_BASE_NAME}.bin
    ln -sf ${BOOTBIN_BASE_NAME}.bin ${DEPLOYDIR}/BOOT-${MACHINE}.bin
    ln -sf ${BOOTBIN_BASE_NAME}.bin ${DEPLOYDIR}/boot.bin
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

FILES:${PN} += "/boot/BOOT.bin"
SYSROOT_DIRS += "/boot"

addtask do_deploy before do_build after do_compile
