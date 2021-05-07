SUMMARY = "U-boot boot scripts for Xilinx devices"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "u-boot-mkimage-native"

inherit deploy nopackages image-wic-utils

INHIBIT_DEFAULT_DEPS = "1"

COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE_zynqmp = "zynqmp"
COMPATIBLE_MACHINE_zynq = "zynq"
COMPATIBLE_MACHINE_versal = "versal"
COMPATIBLE_MACHINE_microblaze = "microblaze"

KERNELDT = "${@os.path.basename(d.getVar('KERNEL_DEVICETREE').split(' ')[0]) if d.getVar('KERNEL_DEVICETREE') else ''}"
DEVICE_TREE_NAME ?= "${@bb.utils.contains('PREFERRED_PROVIDER_virtual/dtb', 'device-tree', 'system.dtb', d.getVar('KERNELDT'), d)}"
#Need to copy a rootfs.cpio.gz.u-boot as uramdisk.image.gz into boot partition
RAMDISK_IMAGE ?= ""
RAMDISK_IMAGE_zynq ?= "uramdisk.image.gz"

PXERAMDISK_IMAGE ?= "${@'ramdisk.cpio.gz.u-boot' if d.getVar('INITRAMFS_IMAGE') and d.getVar('INITRAMFS_IMAGE').find('initramfs') > 0 else '${RAMDISK_IMAGE}'}"

KERNEL_BOOTCMD_zynqmp ?= "booti"
KERNEL_BOOTCMD_zynq ?= "bootm"
KERNEL_BOOTCMD_versal ?= "booti"
KERNEL_BOOTCMD_microblaze ?= "bootm"

BOOTMODE ?= "sd"
BOOTFILE_EXT ?= ".${SOC_FAMILY}"

#Make this value to "1" to skip appending base address to ddr offsets.
SKIP_APPEND_BASEADDR ?= "0"

DDR_BASEADDR ?= "0x0"
DDR_BASEADDR_microblaze ?= "0x80000000"
PRE_BOOTENV ?= ""

SRC_URI = " \
            file://boot.cmd.sd.zynq \
            file://boot.cmd.sd.zynqmp \
            file://boot.cmd.sd.versal \
            file://boot.cmd.qspi.versal \
	    file://boot.cmd.generic \
	    file://boot.cmd.ubifs \
            file://pxeboot.pxe \
            "

# We fall back to MACHINE_ARCH in most cases
FALLBACK_ARCH = "${MACHINE_ARCH}"

# Except on zynqmp-dr, where we need to fall back to SOC_VARIANT_ARCH, which
# falls back to MACHINE_ARCH if necessary
SOC_VARIANT_ARCH ??= "${MACHINE_ARCH}"
FALLBACK_ARCH_zynqmp-dr = "${SOC_VARIANT_ARCH}"

BOARDVARIANT_ARCH ??= "${FALLBACK_ARCH}"
PACKAGE_ARCH = "${BOARDVARIANT_ARCH}"

inherit image-artifact-names
UENV_TEXTFILE ?= "uEnv.txt"
UENV_MMC_OFFSET_zynqmp ?= "0x200000"
UENV_MMC_OFFSET_zynq ?= "0x2080000"
UENV_MMC_OFFSET_versal ?= "0x80000"
UENV_MMC_OFFSET_microblaze ?= "0x0"

UENV_MMC_LOAD_ADDRESS ?= "${@append_baseaddr(d,d.getVar('UENV_MMC_OFFSET'))}"

UBOOTSCR_BASE_NAME ?= "${PN}-${PKGE}-${PKGV}-${PKGR}${IMAGE_VERSION_SUFFIX}"
UBOOTPXE_CONFIG ?= "pxelinux.cfg"
UBOOTPXE_CONFIG_NAME = "${UBOOTPXE_CONFIG}${IMAGE_VERSION_SUFFIX}"

DEVICETREE_ADDRESS ?= "${@append_baseaddr(d,d.getVar('DEVICETREE_OFFSET'))}"

DEVICETREE_OFFSET_microblaze ?= "0x1e00000"
DEVICETREE_OFFSET_zynqmp ?= "0x100000"
DEVICETREE_OFFSET_zynq ?= "0x2000000"
DEVICETREE_OFFSET_versal ?= "0x1000"

KERNEL_LOAD_ADDRESS ?= "${@append_baseaddr(d,d.getVar('KERNEL_OFFSET'))}"

KERNEL_OFFSET_microblaze ?= "0x0"
KERNEL_OFFSET_zynqmp ?= "0x200000"
KERNEL_OFFSET_zynq ?= "0x2080000"
KERNEL_OFFSET_versal ?= "0x80000"

KERNEL_IMAGE ?= "${KERNEL_IMAGETYPE}"

RAMDISK_IMAGE_ADDRESS ?= "${@append_baseaddr(d,d.getVar('RAMDISK_OFFSET'))}"

RAMDISK_OFFSET_microblaze ?= "0x2e00000"
RAMDISK_OFFSET_zynq ?= "0x4000000"
RAMDISK_OFFSET_zynqmp ?= "0x4000000"
RAMDISK_OFFSET_versal ?= "0x6000000"

FIT_IMAGE_LOAD_ADDRESS ?= "${@append_baseaddr(d,d.getVar('FIT_IMAGE_OFFSET'))}"
FIT_IMAGE_OFFSET ?= "0x10000000"
FIT_IMAGE ?= "image.ub"

## Below offsets and sizes are based on 32MB QSPI Memory for zynq
## For MB
## Load boot.scr at 0xFC0000 -> 15MB of QSPI/NAND Memory
QSPI_KERNEL_OFFSET_microblaze ?= "0xBC0000"
QSPI_KERNEL_SIZE_microblaze ?= "0x500000"
QSPI_RAMDISK_SIZE_microblaze ?= "0xA00000"

## For zynq
## Load boot.scr at 0xFC0000 -> 15MB of QSPI/NAND Memory
QSPI_KERNEL_OFFSET_zynq ?= "0x1000000"
QSPI_RAMDISK_OFFSET_zynq ?= "0x1580000"

NAND_KERNEL_OFFSET_zynq ?= "0x1000000"
NAND_RAMDISK_OFFSET_zynq ?= "0x4600000"

QSPI_KERNEL_SIZE_zynq ?= "0x500000"
QSPI_RAMDISK_SIZE_zynq ?= "0xA00000"

NAND_KERNEL_SIZE ?= "0x3200000"
NAND_RAMDISK_SIZE ?= "0x3200000"

## Below offsets and sizes are based on 128MB QSPI Memory for zynqmp/versal
## For zynqMP
## Load boot.scr at 0x3E80000 -> 62MB of QSPI/NAND Memory
QSPI_KERNEL_OFFSET ?= "0xF00000"
QSPI_KERNEL_OFFSET_zynqmp-dr ?= "0x3F00000"
QSPI_RAMDISK_OFFSET ?= "0x4000000"
QSPI_RAMDISK_OFFSET_zynqmp-dr ?= "0x5D00000"

NAND_KERNEL_OFFSET_zynqmp ?= "0x4100000"
NAND_RAMDISK_OFFSET_zynqmp ?= "0x7800000"

QSPI_KERNEL_SIZE_zynqmp ?= "0x1D00000"
QSPI_RAMDISK_SIZE ?= "0x4000000"
QSPI_RAMDISK_SIZE_zynqmp-dr ?= "0x1D00000"

## For versal
## Load boot.scr at 0x7F80000 -> 127MB of QSPI/NAND Memory
QSPI_KERNEL_OFFSET_versal ?= "0xF00000"
QSPI_RAMDISK_OFFSET_versal ?= "0x2E00000"

NAND_KERNEL_OFFSET_versal ?= "0x4100000"
NAND_RAMDISK_OFFSET_versal ?= "0x8200000"

QSPI_KERNEL_SIZE_versal ?= "0x1D00000"
QSPI_RAMDISK_SIZE_versal ?= "0x4000000"

QSPI_KERNEL_IMAGE_microblaze ?= "image.ub"
QSPI_KERNEL_IMAGE_zynq ?= "image.ub"
QSPI_KERNEL_IMAGE_zynqmp ?= "image.ub"
QSPI_KERNEL_IMAGE_versal ?= "image.ub"

NAND_KERNEL_IMAGE ?= "image.ub"

QSPI_FIT_IMAGE_OFFSET ?= "0x1080000"
QSPI_FIT_IMAGE_SIZE ?= "0x6400000"
QSPI_FIT_IMAGE_SIZE_zynqmp-dr ?= "0x3F00000"
QSPI_FIT_IMAGE_SIZE_zynq ?= "0xF00000"
QSPI_FIT_IMAGE_SIZE_microblaze ?= "0xF00000"

NAND_FIT_IMAGE_OFFSET ?= "0x1080000"
NAND_FIT_IMAGE_SIZE ?= "0x6400000"

SDBOOTDEV ?= "0"

BITSTREAM_LOAD_ADDRESS ?= "0x100000"

do_configure[noexec] = "1"
do_install[noexec] = "1"

python () {
    baseaddr = d.getVar('DDR_BASEADDR') or "0x0"
    if baseaddr == "0x0":
        d.appendVar('PRE_BOOTENV','')
    else:
        soc_family = d.getVar('SOC_FAMILY') or ""
        if soc_family == "zynqmp":
            fdt_high = "0x10000000"
        elif soc_family == "zynq":
            fdt_high = "0x20000000"
        elif soc_family == "versal":
            fdt_high = "0x70000000"
        else:
            fdt_high = ""

        if fdt_high:
            basefdt_high = append_baseaddr(d,fdt_high)
            bootenv = "setenv fdt_high " + basefdt_high
            d.appendVar('PRE_BOOTENV',bootenv)
}

def append_baseaddr(d,offset):
    skip_append = d.getVar('SKIP_APPEND_BASEADDR') or ""
    if skip_append == "1":
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
        "${WORKDIR}/boot.cmd.${BOOTMODE}${BOOTFILE_EXT}" > "${WORKDIR}/boot.cmd"
    mkimage -A arm -T script -C none -n "Boot script" -d "${WORKDIR}/boot.cmd" boot.scr
    sed -e 's/@@KERNEL_IMAGETYPE@@/${KERNEL_IMAGETYPE}/' \
        -e 's/@@DEVICE_TREE_NAME@@/${DEVICE_TREE_NAME}/' \
	-e 's/@@RAMDISK_IMAGE@@/${PXERAMDISK_IMAGE}/' \
	"${WORKDIR}/pxeboot.pxe" > "pxeboot.pxe"
}


do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 boot.scr ${DEPLOYDIR}/${UBOOTSCR_BASE_NAME}.scr
    ln -sf ${UBOOTSCR_BASE_NAME}.scr ${DEPLOYDIR}/boot.scr
    install -d ${DEPLOYDIR}/pxeboot/${UBOOTPXE_CONFIG_NAME}
    install -m 0644 pxeboot.pxe ${DEPLOYDIR}/pxeboot/${UBOOTPXE_CONFIG_NAME}/default
    ln -sf pxeboot/${UBOOTPXE_CONFIG_NAME} ${DEPLOYDIR}/${UBOOTPXE_CONFIG}
}

addtask do_deploy after do_compile before do_build
