SUMMARY = "U-boot boot scripts for Xilinx devices"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "u-boot-mkimage-native"

inherit deploy image-wic-utils

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:zynq = "zynq"
COMPATIBLE_MACHINE:versal = "versal"
COMPATIBLE_MACHINE:versal-net = "versal"
COMPATIBLE_MACHINE:microblaze = "microblaze"

KERNELDT = "${@os.path.basename(d.getVar('KERNEL_DEVICETREE').split(' ')[0]) if d.getVar('KERNEL_DEVICETREE') else ''}"
DEVICE_TREE_NAME ?= "${@bb.utils.contains('PREFERRED_PROVIDER_virtual/dtb', 'device-tree', 'system.dtb', d.getVar('KERNELDT'), d)}"
#Need to copy a rootfs.cpio.gz.u-boot  into boot partition
RAMDISK_IMAGE ?= "rootfs.cpio.gz.u-boot"
RAMDISK_IMAGE1 ?= "ramdisk.cpio.gz.u-boot"

PXERAMDISK_IMAGE ?= "${@'${RAMDISK_IMAGE1}' if d.getVar('INITRAMFS_IMAGE') and d.getVar('INITRAMFS_IMAGE').find('initramfs') > 0 else '${RAMDISK_IMAGE}'}"

KERNEL_BOOTCMD:zynqmp ?= "booti"
KERNEL_BOOTCMD:zynq ?= "bootm"
KERNEL_BOOTCMD:versal ?= "booti"
KERNEL_BOOTCMD:versal-net ?= "booti"
KERNEL_BOOTCMD:microblaze ?= "bootm"

BOOTMODE ??= "generic"
BOOTFILE_EXT ?= ""

#Make this value to "1" to skip appending base address to ddr offsets.
SKIP_APPEND_BASEADDR ?= "0"

DDR_BASEADDR ?= "0x0"
DDR_BASEADDR:microblaze ?= "0x80000000"
PRE_BOOTENV ?= ""

# Set debfault SD boot device to mmc 0 interface
SDBOOTDEV ?= "0"

SRC_URI = " \
    file://boot.cmd.sd.zynq \
    file://boot.cmd.sd.zynqmp \
    file://boot.cmd.sd.versal \
    file://boot.cmd.qspi.versal \
    file://boot.cmd.generic \
    file://boot.cmd.generic.root \
    file://boot.cmd.ubifs \
    file://pxeboot.pxe \
    "

# Even thought we don't create a package, make sure this is unique to the machine
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit image-artifact-names
UENV_TEXTFILE ?= "uEnv.txt"
UENV_MMC_OFFSET:zynqmp ??= "0x200000"
UENV_MMC_OFFSET:zynq ??= "0x2080000"
UENV_MMC_OFFSET:versal ??= "0x200000"
UENV_MMC_OFFSET:versal-net ??= "0x200000"
UENV_MMC_OFFSET:microblaze ??= "0x0"

UENV_MMC_LOAD_ADDRESS ?= "${@append_baseaddr(d,d.getVar('UENV_MMC_OFFSET'))}"

UBOOTSCR_BASE_NAME ?= "${PN}-${PKGE}-${PKGV}-${PKGR}${IMAGE_VERSION_SUFFIX}"
UBOOTPXE_CONFIG ?= "pxelinux.cfg"
UBOOTPXE_CONFIG_NAME = "${UBOOTPXE_CONFIG}${IMAGE_VERSION_SUFFIX}"

DEVICETREE_ADDRESS ?= "${@append_baseaddr(d,d.getVar('DEVICETREE_OFFSET'))}"
DEVICETREE_ADDRESS_SD ?= "${DEVICETREE_ADDRESS}"

DEVICETREE_OFFSET:microblaze ??= "0x1e00000"
DEVICETREE_OFFSET:zynqmp ??= "0x100000"
DEVICETREE_OFFSET:zynq ??= "0x100000"
DEVICETREE_OFFSET:versal ??= "0x1000"
DEVICETREE_OFFSET:versal-net ??= "0x1000"

DEVICETREE_OVERLAY_OFFSET:microblaze ??= "0x1e00000"
DEVICETREE_OVERLAY_OFFSET:zynqmp ??= "0x100000"
DEVICETREE_OVERLAY_OFFSET:zynq ??= "0x100000"
DEVICETREE_OVERLAY_OFFSET:versal ??= "0x1000"
DEVICETREE_OVERLAY_OFFSET:versal-net ??= "0x1000"
DEVICETREE_OVERLAY_PADSIZE ??= "0x1f00000"

DEVICETREE_OVERLAY_ADDRESS ?= "${@hex(int(append_baseaddr(d,d.getVar('DEVICETREE_OVERLAY_OFFSET')),16) \
				+ int(d.getVar('DEVICETREE_OVERLAY_PADSIZE'),16))}"

KERNEL_LOAD_ADDRESS ?= "${@append_baseaddr(d,d.getVar('KERNEL_OFFSET'))}"

KERNEL_OFFSET:microblaze ??= "0x0"
KERNEL_OFFSET:zynqmp ??= "0x200000"
KERNEL_OFFSET:zynq ??= "0x200000"
KERNEL_OFFSET:versal ??= "0x200000"
KERNEL_OFFSET:versal-net ??= "0x200000"

KERNEL_IMAGE ?= "${KERNEL_IMAGETYPE}"

RAMDISK_IMAGE_ADDRESS ?= "${@append_baseaddr(d,d.getVar('RAMDISK_OFFSET'))}"

RAMDISK_OFFSET:microblaze ??= "0x2e00000"
RAMDISK_OFFSET:zynq ??= "0x4000000"
RAMDISK_OFFSET:zynqmp ??= "0x4000000"
RAMDISK_OFFSET:versal ??= "0x4000000"
RAMDISK_OFFSET:versal-net ??= "0x4000000"

FIT_IMAGE_LOAD_ADDRESS ?= "${@append_baseaddr(d,d.getVar('FIT_IMAGE_OFFSET'))}"
FIT_IMAGE_OFFSET ??= "0x10000000"
FIT_IMAGE ?= "image.ub"

## Below offsets and sizes are based on 32MB QSPI Memory for zynq
## For MB
## Load boot.scr at 0xFC0000 -> 15MB of QSPI/NAND Memory
QSPI_KERNEL_OFFSET:microblaze ??= "0xBC0000"
QSPI_KERNEL_SIZE:microblaze ??= "0x500000"
QSPI_RAMDISK_SIZE:microblaze ??= "0xA00000"
QSPI_RAMDISK_SIZE:microblaze ??= "0x4000000"

## For zynq
## Load boot.scr at 0xFC0000 -> 15MB of QSPI/NAND Memory
QSPI_KERNEL_OFFSET:zynq ??= "0xA00000"
QSPI_RAMDISK_OFFSET:zynq ??= "0x1000000"

NAND_KERNEL_OFFSET:zynq ??= "0x1000000"
NAND_RAMDISK_OFFSET:zynq ??= "0x4600000"

QSPI_KERNEL_SIZE:zynq ??= "0x600000"
QSPI_RAMDISK_SIZE:zynq ??= "0xF80000"

NAND_KERNEL_SIZE ??= "0x3200000"
NAND_RAMDISK_SIZE ??= "0x3200000"

## Below offsets and sizes are based on 128MB QSPI Memory for zynqmp/versal
## For zynqMP
## Load boot.scr at 0x3E80000 -> 62MB of QSPI/NAND Memory
QSPI_KERNEL_OFFSET ??= "0xF00000"
QSPI_KERNEL_OFFSET:zynqmpdr ??= "0x3F00000"
QSPI_RAMDISK_OFFSET ??= "0x4000000"
QSPI_RAMDISK_OFFSET:zynqmpdr ??= "0x5D00000"

NAND_KERNEL_OFFSET:zynqmp ??= "0x4100000"
NAND_RAMDISK_OFFSET:zynqmp ??= "0x7800000"

QSPI_KERNEL_SIZE:zynqmp ??= "0x1D00000"
QSPI_RAMDISK_SIZE ??= "0x4000000"
QSPI_RAMDISK_SIZE:zynqmpdr ??= "0x1D00000"

## For versal
## Load boot.scr at 0x7F80000 -> 127MB of QSPI/NAND Memory
QSPI_KERNEL_OFFSET:versal ??= "0xF00000"
QSPI_KERNEL_OFFSET:versal-net ??= "0xF00000"
QSPI_RAMDISK_OFFSET:versal ??= "0x2E00000"
QSPI_RAMDISK_OFFSET:versal-net ??= "0x2E00000"

NAND_KERNEL_OFFSET:versal ??= "0x4100000"
NAND_KERNEL_OFFSET:versal-net ??= "0x4100000"
NAND_RAMDISK_OFFSET:versal ??= "0x8200000"
NAND_RAMDISK_OFFSET:versal-net ??= "0x8200000"

QSPI_KERNEL_SIZE:versal ??= "0x1D00000"
QSPI_KERNEL_SIZE:versal-net ??= "0x1D00000"
QSPI_RAMDISK_SIZE:versal ??= "0x4000000"
QSPI_RAMDISK_SIZE:versal-net ??= "0x4000000"

QSPI_KERNEL_IMAGE:microblaze ?= "image.ub"
QSPI_KERNEL_IMAGE:zynq ?= "image.ub"
QSPI_KERNEL_IMAGE:zynqmp ?= "image.ub"
QSPI_KERNEL_IMAGE:versal ?= "image.ub"
QSPI_KERNEL_IMAGE:versal-net ?= "image.ub"

NAND_KERNEL_IMAGE ?= "image.ub"

QSPI_FIT_IMAGE_OFFSET ??= "0xF40000"
QSPI_FIT_IMAGE_OFFSET:zynqmpdr ??= "0x3F80000"
QSPI_FIT_IMAGE_OFFSET:zynq ??= "0xA80000"
QSPI_FIT_IMAGE_OFFSET:microblaze ??= "0xC00000"

QSPI_FIT_IMAGE_SIZE ??= "0x6400000"
QSPI_FIT_IMAGE_SIZE:zynqmpdr ??= "0x3F00000"
QSPI_FIT_IMAGE_SIZE:zynq ??= "0x1500000"
QSPI_FIT_IMAGE_SIZE:microblaze ??= "0xF00000"

NAND_FIT_IMAGE_OFFSET ??= "0x4180000"
NAND_FIT_IMAGE_OFFSET:zynq ??= "0x1080000"
NAND_FIT_IMAGE_SIZE ??= "0x6400000"

# Add variables as addendum.
SCRIPT_SED_ADDENDUM = ""

# Default to booting with the rootfs device being partition 2 for SD/eMMC
PARTNUM ?= "2"

# Set Kernel root filesystem parameter for SD/eMMC boot
# Bootdev will automatically be set to 'sda' or 'mmcblkXp'
KERNEL_ROOT_SD ?= "root=/dev/\${bootdev}${PARTNUM} ro rootwait"

# Set Kernel root filesystem parameter for JTAG/QSPI/OSPI/NAND(using RAMDISK) boot
KERNEL_ROOT_RAMDISK ?= "root=/dev/ram0 rw"

# Append the kernel command line
KERNEL_COMMAND_APPEND ?= ""

BITSTREAM_LOAD_ADDRESS ?= "0x100000"

do_configure[noexec] = "1"

def append_baseaddr(d,offset):
    skip_append = d.getVar('SKIP_APPEND_BASEADDR') or ""
    if skip_append == "1":
        return offset
    if offset.startswith('$'):
        # If offset startswith '$' Assuming as uboot env variable.
        return offset
    import subprocess
    baseaddr = d.getVar('DDR_BASEADDR') or "0x0"
    subcmd = "$((%s+%s));" % (baseaddr,offset)
    cmd = "printf '0x%08x' " + str(subcmd)
    output = subprocess.check_output(cmd, shell=True).decode("utf-8")
    return output

def get_bitstream_load_type(d):
    if boot_files_bitstream(d)[1] :
        return "loadb"
    else:
        return "load"

do_compile() {
    sed -e 's/@@KERNEL_IMAGETYPE@@/${KERNEL_IMAGETYPE}/' \
        -e 's/@@KERNEL_LOAD_ADDRESS@@/${KERNEL_LOAD_ADDRESS}/' \
        -e 's/@@DEVICE_TREE_NAME@@/${DEVICE_TREE_NAME}/' \
        -e 's/@@DEVICETREE_ADDRESS@@/${DEVICETREE_ADDRESS}/' \
	-e 's/@@DEVICETREE_ADDRESS_SD@@/${DEVICETREE_ADDRESS_SD}/' \
        -e 's/@@DEVICETREE_OVERLAY_ADDRESS@@/${DEVICETREE_OVERLAY_ADDRESS}/' \
        -e 's/@@RAMDISK_IMAGE@@/${RAMDISK_IMAGE}/' \
        -e 's/@@RAMDISK_IMAGE_ADDRESS@@/${RAMDISK_IMAGE_ADDRESS}/' \
        -e 's/@@KERNEL_BOOTCMD@@/${KERNEL_BOOTCMD}/' \
        -e 's/@@SDBOOTDEV@@/${SDBOOTDEV}/' \
        -e 's/@@BITSTREAM@@/${@boot_files_bitstream(d)[0]}/g' \
        -e 's/@@BITSTREAM_LOAD_ADDRESS@@/${BITSTREAM_LOAD_ADDRESS}/g' \
        -e 's/@@BITSTREAM_IMAGE@@/${@boot_files_bitstream(d)[0]}/g' \
        -e 's/@@BITSTREAM_LOAD_TYPE@@/${@get_bitstream_load_type(d)}/g' \
        -e 's/@@QSPI_KERNEL_OFFSET@@/${QSPI_KERNEL_OFFSET}/' \
        -e 's/@@NAND_KERNEL_OFFSET@@/${NAND_KERNEL_OFFSET}/' \
        -e 's/@@QSPI_KERNEL_SIZE@@/${QSPI_KERNEL_SIZE}/' \
        -e 's/@@NAND_KERNEL_SIZE@@/${NAND_KERNEL_SIZE}/' \
        -e 's/@@QSPI_RAMDISK_OFFSET@@/${QSPI_RAMDISK_OFFSET}/' \
        -e 's/@@NAND_RAMDISK_OFFSET@@/${NAND_RAMDISK_OFFSET}/' \
        -e 's/@@QSPI_RAMDISK_SIZE@@/${QSPI_RAMDISK_SIZE}/' \
        -e 's/@@NAND_RAMDISK_SIZE@@/${NAND_RAMDISK_SIZE}/' \
        -e 's/@@KERNEL_IMAGE@@/${KERNEL_IMAGE}/' \
        -e 's/@@QSPI_KERNEL_IMAGE@@/${QSPI_KERNEL_IMAGE}/' \
        -e 's/@@NAND_KERNEL_IMAGE@@/${NAND_KERNEL_IMAGE}/' \
        -e 's/@@FIT_IMAGE_LOAD_ADDRESS@@/${FIT_IMAGE_LOAD_ADDRESS}/' \
        -e 's/@@QSPI_FIT_IMAGE_OFFSET@@/${QSPI_FIT_IMAGE_OFFSET}/' \
        -e 's/@@QSPI_FIT_IMAGE_SIZE@@/${QSPI_FIT_IMAGE_SIZE}/' \
        -e 's/@@NAND_FIT_IMAGE_OFFSET@@/${NAND_FIT_IMAGE_OFFSET}/' \
        -e 's/@@NAND_FIT_IMAGE_SIZE@@/${NAND_FIT_IMAGE_SIZE}/' \
        -e 's/@@FIT_IMAGE@@/${FIT_IMAGE}/' \
        -e 's/@@PRE_BOOTENV@@/${PRE_BOOTENV}/' \
        -e 's/@@UENV_MMC_LOAD_ADDRESS@@/${UENV_MMC_LOAD_ADDRESS}/' \
        -e 's/@@UENV_TEXTFILE@@/${UENV_TEXTFILE}/' \
        -e 's/@@RAMDISK_IMAGE1@@/${RAMDISK_IMAGE1}/' \
        -e 's/@@PARTNUM@@/${PARTNUM}/' \
        -e 's:@@KERNEL_ROOT_SD@@:${KERNEL_ROOT_SD}:' \
        -e 's:@@KERNEL_ROOT_RAMDISK@@:${KERNEL_ROOT_RAMDISK}:' \
        -e 's:@@KERNEL_COMMAND_APPEND@@:${KERNEL_COMMAND_APPEND}:' \
        ${SCRIPT_SED_ADDENDUM} \
        "${WORKDIR}/boot.cmd.${BOOTMODE}${BOOTFILE_EXT}" > "${WORKDIR}/boot.cmd"

    mkimage -A arm -T script -C none -n "Boot script" -d "${WORKDIR}/boot.cmd" boot.scr

    sed -e 's/@@KERNEL_IMAGETYPE@@/${KERNEL_IMAGETYPE}/' \
        -e 's/@@DEVICE_TREE_NAME@@/${DEVICE_TREE_NAME}/' \
        -e 's/@@RAMDISK_IMAGE@@/${PXERAMDISK_IMAGE}/' \
        "${WORKDIR}/pxeboot.pxe" > "pxeboot.pxe"
}

do_install() {
    install -d ${D}/boot
    install -m 0644 boot.scr ${D}/boot/${UBOOTSCR_BASE_NAME}.scr
    install -m 0644 boot.scr ${D}/boot/
    install -d ${D}/boot/pxeboot/${UBOOTPXE_CONFIG_NAME}
    install -m 0644 pxeboot.pxe ${D}/boot/pxeboot/${UBOOTPXE_CONFIG_NAME}/default
    install -d ${D}/boot/${UBOOTPXE_CONFIG}/
    install -m 0644 pxeboot.pxe ${D}/boot/${UBOOTPXE_CONFIG}/default
}

FILES:${PN} = "/boot/*"

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 boot.scr ${DEPLOYDIR}/${UBOOTSCR_BASE_NAME}.scr
    install -m 0644 boot.scr ${DEPLOYDIR}/
    install -d ${DEPLOYDIR}/pxeboot/${UBOOTPXE_CONFIG_NAME}
    install -m 0644 pxeboot.pxe ${DEPLOYDIR}/pxeboot/${UBOOTPXE_CONFIG_NAME}/default
    install -d ${DEPLOYDIR}/${UBOOTPXE_CONFIG}/
    install -m 0644 pxeboot.pxe ${DEPLOYDIR}/${UBOOTPXE_CONFIG}/default
}

addtask do_deploy after do_compile before do_build
