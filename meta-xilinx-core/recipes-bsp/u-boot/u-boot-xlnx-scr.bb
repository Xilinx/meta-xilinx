SUMMARY = "U-boot boot scripts for Xilinx devices"
DESCRIPTION = "Generates the U-Boot boot.scr / boot.scr.uimg used by \
AMD Xilinx Zynq, Zynq UltraScale+ MPSoC, Versal and MicroBlaze boards \
to load the kernel, device tree and ramdisk during boot."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "u-boot-mkimage-native"

inherit deploy image-wic-utils

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:arm = ".*"
COMPATIBLE_MACHINE:aarch64 = ".*"
COMPATIBLE_MACHINE:microblaze-v = ".*"

KERNELDT = "${@os.path.basename(d.getVar('KERNEL_DEVICETREE').split(' ')[0]) if d.getVar('KERNEL_DEVICETREE') else ''}"
DEVICE_TREE_NAME ?= "${@bb.utils.contains('PREFERRED_PROVIDER_virtual/dtb', 'device-tree', 'system.dtb', d.getVar('KERNELDT'), d)}"

# This is required for pxeboot and default to core-image-cmdline.
RAMDISK_IMAGE ?= "core-image-full-cmdline-${MACHINE}-rootfs.cpio.gz.u-boot"

DEFAULT_KERNEL_BOOTCMD ??= "booti"
DEFAULT_KERNEL_BOOTCMD:arm ??= "bootm"
KERNEL_BOOTCMD ?= "${DEFAULT_KERNEL_BOOTCMD}"

#Make this value to "1" to skip appending base address to ddr offsets.
SKIP_APPEND_BASEADDR ?= "0"

DDR_BASEADDR ?= "0x0"
PRE_BOOTENV ?= ""

# Set debfault SD boot device to mmc 0 interface
SDBOOTDEV ?= "0"

SRC_URI = " \
    file://pxeboot.pxe \
    file://xilinx-default-sd.cmd \
    "

# Even thought we don't create a package, make sure this is unique to the machine
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit image-artifact-names

# Boot script template compiled by do_compile(): UBOOT_XLNX_BOOT_SCR names
# the file (under this recipe's SRC_URI) used as input, defaulting to
# xilinx-default-sd.cmd on arm/aarch64. Override UBOOT_XLNX_BOOT_SCR (or
# DEFAULT_UBOOT_XLNX_BOOT_SCR) in a bbappend to select a different template.
DEFAULT_UBOOT_XLNX_BOOT_SCR ??= "xilinx-default-sd.cmd"
UBOOT_XLNX_BOOT_SCR ?= "${DEFAULT_UBOOT_XLNX_BOOT_SCR}"

UENV_TEXTFILE ?= "uEnv.txt"
UENV_MMC_OFFSET:zynqmp ??= "0x200000"
UENV_MMC_OFFSET:zynq ??= "0x2080000"
UENV_MMC_OFFSET:versal ??= "0x200000"
UENV_MMC_OFFSET:versal-net ??= "0x200000"
UENV_MMC_OFFSET:versal-2ve-2vm ??= "0x200000"
UENV_MMC_OFFSET:versal-2vp ??= "0x200000"
UENV_MMC_OFFSET:microblaze-v ??= "0x0"

UENV_MMC_LOAD_ADDRESS ?= "${@append_baseaddr(d,d.getVar('UENV_MMC_OFFSET'))}"

UBOOTSCR_BASE_NAME ?= "${PN}-${PKGE}-${PKGV}-${PKGR}${IMAGE_VERSION_SUFFIX}"
UBOOTPXE_CONFIG ?= "pxelinux.cfg"
UBOOTPXE_CONFIG_NAME = "${UBOOTPXE_CONFIG}${IMAGE_VERSION_SUFFIX}"

DEVICETREE_ADDRESS ?= "${@append_baseaddr(d,d.getVar('DEVICETREE_OFFSET'))}"

DEVICETREE_OFFSET_DEFAULT ??= ""
DEVICETREE_OFFSET_DEFAULT:zynqmp ??= "0x100000"
DEVICETREE_OFFSET_DEFAULT:zynq ??= "0x100000"
DEVICETREE_OFFSET_DEFAULT:versal ??= "0x1000"
DEVICETREE_OFFSET_DEFAULT:versal-net ??= "0x1000"
DEVICETREE_OFFSET_DEFAULT:versal-2ve-2vm ??= "0x1000"
DEVICETREE_OFFSET_DEFAULT:versal-2vp ??= "0x1000"
DEVICETREE_OFFSET_DEFAULT:microblaze-v ??= "0x1e00000"
DEVICETREE_OFFSET ??= "${DEVICETREE_OFFSET_DEFAULT}"

KERNEL_LOAD_ADDRESS ?= "${@append_baseaddr(d,d.getVar('KERNEL_OFFSET'))}"


KERNEL_OFFSET_DEFAULT ??= ""
KERNEL_OFFSET_DEFAULT:zynqmp ??= "0x200000"
KERNEL_OFFSET_DEFAULT:zynq ??= "0x200000"
KERNEL_OFFSET_DEFAULT:versal ??= "0x200000"
KERNEL_OFFSET_DEFAULT:versal-net ??= "0x200000"
KERNEL_OFFSET_DEFAULT:versal-2ve-2vm ??= "0x200000"
KERNEL_OFFSET_DEFAULT:versal-2vp ??= "0x200000"
KERNEL_OFFSET ??= "${KERNEL_OFFSET_DEFAULT}"

KERNEL_IMAGE ?= "${KERNEL_IMAGETYPE}"

RAMDISK_IMAGE_ADDRESS ?= "${@append_baseaddr(d,d.getVar('RAMDISK_OFFSET'))}"

RAMDISK_OFFSET_DEFAULT ??= ""
RAMDISK_OFFSET_DEFAULT:zynq ??= "0x4000000"
RAMDISK_OFFSET_DEFAULT:zynqmp ??= "0x4000000"
RAMDISK_OFFSET_DEFAULT:versal ??= "0x4000000"
RAMDISK_OFFSET_DEFAULT:versal-net ??= "0x4000000"
RAMDISK_OFFSET_DEFAULT:versal-2ve-2vm ??= "0x4000000"
RAMDISK_OFFSET_DEFAULT:versal-2vp ??= "0x4000000"
RAMDISK_OFFSET ??= "${RAMDISK_OFFSET_DEFAULT}"

## Below offsets and sizes are based on 32MB QSPI Memory for zynq
## For zynq
## Load boot.scr at 0xFC0000 -> 15MB of QSPI/NAND Memory
SPI_KERNEL_OFFSET:zynq ??= "0xA00000"
SPI_RAMDISK_OFFSET:zynq ??= "0x1000000"

NAND_KERNEL_OFFSET:zynq ??= "0x1000000"
NAND_RAMDISK_OFFSET:zynq ??= "0x4600000"

SPI_KERNEL_SIZE:zynq ??= "0x600000"
SPI_RAMDISK_SIZE:zynq ??= "0xF80000"

NAND_KERNEL_SIZE ??= "0x3200000"
NAND_RAMDISK_SIZE ??= "0x3200000"

## Below offsets and sizes are based on 128MB QSPI/OSPI Memory for zynqmp/versal
## For zynqMP
## Load boot.scr at 0x3E80000 -> 62MB of QSPI/NAND Memory
SPI_KERNEL_OFFSET ??= "0xF00000"
SPI_KERNEL_OFFSET:zynqmpdr ??= "0x3F00000"
SPI_RAMDISK_OFFSET ??= "0x4000000"
SPI_RAMDISK_OFFSET:zynqmpdr ??= "0x5D00000"

NAND_KERNEL_OFFSET:zynqmp ??= "0x4100000"
NAND_RAMDISK_OFFSET:zynqmp ??= "0x7800000"

SPI_KERNEL_SIZE:zynqmp ??= "0x1D00000"
SPI_RAMDISK_SIZE ??= "0x4000000"
SPI_RAMDISK_SIZE:zynqmpdr ??= "0x1D00000"

## For versal
## Load boot.scr at 0x7F80000 -> 127MB of QSPI/OSPI/NAND Memory
SPI_KERNEL_OFFSET:versal ??= "0xF00000"
SPI_KERNEL_OFFSET:versal-net ??= "0xF00000"
SPI_KERNEL_OFFSET:versal-2ve-2vm ??= "0xF00000"
SPI_KERNEL_OFFSET:versal-2vp ??= "0xF00000"
SPI_RAMDISK_OFFSET:versal ??= "0x2E00000"
SPI_RAMDISK_OFFSET:versal-net ??= "0x2E00000"
SPI_RAMDISK_OFFSET:versal-2ve-2vm ??= "0x2E00000"
SPI_RAMDISK_OFFSET:versal-2vp ??= "0x2E00000"

NAND_KERNEL_OFFSET:versal ??= "0x4100000"
NAND_KERNEL_OFFSET:versal-net ??= "0x4100000"
NAND_KERNEL_OFFSET:versal-2ve-2vm ??= "0x4100000"
NAND_KERNEL_OFFSET:versal-2vp ??= "0x4100000"
NAND_RAMDISK_OFFSET:versal ??= "0x8200000"
NAND_RAMDISK_OFFSET:versal-net ??= "0x8200000"
NAND_RAMDISK_OFFSET:versal-2ve-2vm ??= "0x8200000"
NAND_RAMDISK_OFFSET:versal-2vp ??= "0x8200000"

SPI_KERNEL_SIZE:versal ??= "0x1D00000"
SPI_KERNEL_SIZE:versal-net ??= "0x1D00000"
SPI_KERNEL_SIZE:versal-2ve-2vm ??= "0x1D00000"
SPI_KERNEL_SIZE:versal-2vp ??= "0x1D00000"
SPI_RAMDISK_SIZE:versal ??= "0x4000000"
SPI_RAMDISK_SIZE:versal-net ??= "0x4000000"
SPI_RAMDISK_SIZE:versal-2ve-2vm ??= "0x4000000"
SPI_RAMDISK_SIZE:versal-2vp ??= "0x4000000"

# Add variables as addendum.
SCRIPT_SED_ADDENDUM = ""

# Partition number of the root filesystem, used by SD/eMMC wic layouts
# where the kernel and boot.scr live under /boot on the same ext4 partition
# as the rootfs (as opposed to a separate FAT boot partition addressed
# via distro_bootpart below). Also used to build the kernel root= parameter
# for SD/eMMC boot, and substituted into the @@PARTNUM@@ token still used
# by some downstream boot.cmd templates (e.g. meta-kria's boot.cmd.kria.prod).
ROOT_PARTNUM ?= "2"

# Set Kernel root filesystem parameter for JTAG/QSPI/OSPI/NAND(using RAMDISK) boot
KERNEL_ROOT_RAMDISK ?= "root=/dev/ram0 rw"

# Append the kernel command line
KERNEL_COMMAND_APPEND ?= ""

BITSTREAM_LOAD_ADDRESS ?= "0x100000"

do_configure[noexec] = "1"

S = "${UNPACKDIR}"

def append_baseaddr(d,offset):
    skip_append = d.getVar('SKIP_APPEND_BASEADDR') or ""
    if skip_append == "1":
        return offset
    if offset and offset.startswith('$'):
        # If offset startswith '$' Assuming as uboot env variable.
        return offset
    import subprocess
    baseaddr = d.getVar('DDR_BASEADDR') or "0x0"

    if not offset:
        return baseaddr

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
        -e 's/@@RAMDISK_IMAGE@@/${RAMDISK_IMAGE}/' \
        -e 's/@@RAMDISK_IMAGE_ADDRESS@@/${RAMDISK_IMAGE_ADDRESS}/' \
        -e 's/@@KERNEL_BOOTCMD@@/${KERNEL_BOOTCMD}/' \
        -e 's/@@SDBOOTDEV@@/${SDBOOTDEV}/' \
        -e 's/@@BITSTREAM@@/${@boot_files_bitstream(d)[0]}/g' \
        -e 's/@@BITSTREAM_LOAD_ADDRESS@@/${BITSTREAM_LOAD_ADDRESS}/g' \
        -e 's/@@BITSTREAM_IMAGE@@/${@boot_files_bitstream(d)[0]}/g' \
        -e 's/@@BITSTREAM_LOAD_TYPE@@/${@get_bitstream_load_type(d)}/g' \
        -e 's/@@SPI_KERNEL_OFFSET@@/${SPI_KERNEL_OFFSET}/' \
        -e 's/@@NAND_KERNEL_OFFSET@@/${NAND_KERNEL_OFFSET}/' \
        -e 's/@@SPI_KERNEL_SIZE@@/${SPI_KERNEL_SIZE}/' \
        -e 's/@@NAND_KERNEL_SIZE@@/${NAND_KERNEL_SIZE}/' \
        -e 's/@@SPI_RAMDISK_OFFSET@@/${SPI_RAMDISK_OFFSET}/' \
        -e 's/@@NAND_RAMDISK_OFFSET@@/${NAND_RAMDISK_OFFSET}/' \
        -e 's/@@SPI_RAMDISK_SIZE@@/${SPI_RAMDISK_SIZE}/' \
        -e 's/@@NAND_RAMDISK_SIZE@@/${NAND_RAMDISK_SIZE}/' \
        -e 's/@@KERNEL_IMAGE@@/${KERNEL_IMAGE}/' \
        -e 's/@@PRE_BOOTENV@@/${PRE_BOOTENV}/' \
        -e 's/@@UENV_MMC_LOAD_ADDRESS@@/${UENV_MMC_LOAD_ADDRESS}/' \
        -e 's/@@UENV_TEXTFILE@@/${UENV_TEXTFILE}/' \
        -e 's/@@ROOT_PARTNUM@@/${ROOT_PARTNUM}/' \
        -e 's:@@KERNEL_ROOT_RAMDISK@@:${KERNEL_ROOT_RAMDISK}:' \
        -e 's:@@KERNEL_COMMAND_APPEND@@:${KERNEL_COMMAND_APPEND}:' \
        ${SCRIPT_SED_ADDENDUM} \
        "${UNPACKDIR}/${UBOOT_XLNX_BOOT_SCR}" > "${UNPACKDIR}/boot.cmd"

    mkimage -A arm -T script -C none -n "Default Boot script" -d "${UNPACKDIR}/boot.cmd" boot.scr

    sed -e 's/@@KERNEL_IMAGETYPE@@/${KERNEL_IMAGETYPE}/' \
        -e 's/@@DEVICE_TREE_NAME@@/${DEVICE_TREE_NAME}/' \
        -e 's/@@RAMDISK_IMAGE@@/${RAMDISK_IMAGE}/' \
        "${UNPACKDIR}/pxeboot.pxe" > "pxeboot.pxe"
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
