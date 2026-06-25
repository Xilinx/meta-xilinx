SUMMARY = "AMD Xilinx U-Boot fork built for the image-recovery boot \
path."

DESCRIPTION = "Cut-down build of the AMD Xilinx U-Boot fork used as \
the second-stage bootloader in the image-recovery flow: only the \
minimum needed to boot the recovery kernel and re-flash the primary \
boot partition."
require ${LAYERPATH_xilinx}/recipes-bsp/u-boot/u-boot-xlnx.inc
require ${LAYERPATH_xilinx}/recipes-bsp/u-boot/u-boot-spl-zynq-init.inc
require ${LAYERPATH_xilinx}/recipes-bsp/u-boot/u-boot-xlnx-2025.1.inc

UBOOT_MACHINE_IMGRCVRY_DEF ?= ""
UBOOT_MACHINE_IMGRCVRY_DEF:versal-2ve-2vm ?= "imgrcvry_versal_2ve_2vm_defconfig"

UBOOT_MACHINE_IMGRCVRY ?= "${UBOOT_MACHINE_IMGRCVRY_DEF}"

# Provide a default to avoid parse error with UBOOT_MACHINE not being expandable
UBOOT_MACHINE ??= "none"
UBOOT_MACHINE := "${@'${UBOOT_MACHINE_IMGRCVRY}' if d.getVar('UBOOT_MACHINE_IMGRCVRY') else '${UBOOT_MACHINE}'}"

PREBOOT_CFG_FILE ?= "file://preboot_cmd.cfg"
PREBOOT_CFG_FILE:versal-2ve-2vm ?= "file://preboot_cmd_versal_2ve_2vm.cfg"

SRC_URI:append = " ${@'file://${UBOOT_MACHINE_IMGRCVRY}' if d.getVar('UBOOT_MACHINE_IMGRCVRY') else ''}"
SRC_URI:append = " ${PREBOOT_CFG_FILE}"

do_unpack:append () {
    bb.build.exec_func('do_copy_defconfig', d)
}

do_copy_defconfig () {
        if [ -n "${UBOOT_MACHINE_IMGRCVRY}" ]; then
                cp ${WORKDIR}/${UBOOT_MACHINE_IMGRCVRY} ${S}/configs/
        fi
}


