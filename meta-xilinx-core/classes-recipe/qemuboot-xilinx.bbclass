
# enable the overrides for the context of the conf only
OVERRIDES .= ":qemuboot-xilinx"

# Default machine targets for Xilinx QEMU (FDT Generic)
QB_RNG=""

DEFAULT_QB_SYSTEM_NAME = "qemu-system-amd-fpga-multiarch"
DEFAULT_QB_SYSTEM_NAME:microblaze = "qemu-system-microblazeel"
DEFAULT_QB_SYSTEM_NAME:microblaze-v:riscv32 = "qemu-system-riscv32"
DEFAULT_QB_SYSTEM_NAME:microblaze-v:riscv64 = "qemu-system-riscv64"
QB_SYSTEM_NAME ?= "${DEFAULT_QB_SYSTEM_NAME}"

QB_DEFAULT_FSTYPE ?= "${@qemu_rootfs_params(d,'fstype')}"
QB_ROOTFS ?= "${@qemu_rootfs_params(d,'rootfs')}"
QB_ROOTFS_OPT ?= "${@qemu_rootfs_params(d,'rootfs-opt')}"
QB_DTB ?= "${@qemu_default_dtb(d)}"

# Define the QEMU flash settings, may end up being blank
QEMU_HW_FLASH ?= "${@qemu_mtd_params(d)}"
QEMU_HW_FLASH[docs] = "MTD qemu arguments, usually generated"

QB_OPT_APPEND += "${QEMU_HW_FLASH}"

# https://docs.amd.com/r/en-US/ug585-zynq-7000-SoC-TRM/Boot-Mode-Pin-Settings
# https://docs.amd.com/r/en-US/ug1085-zynq-ultrascale-trm/Boot-Modes
# https://docs.amd.com/r/en-US/am011-versal-acap-trm/Primary-Boot-Interfaces-Table
# https://docs.amd.com/r/en-US/am026-versal-ai-edge-prime-gen2-trm/Primary-Boot-Interfaces-Table
QEMU_HW_BOOT_MODE ?= "${HW_BOOT_MODE}"
QEMU_HW_BOOT_MODE[doc] = "Boot mode value for qemu booting"

# We may need to configure a secondary storage media, if primary is not
# a disk
DEFAULT_SECONDARY_BOOT_MODE:zynq = "5"
DEFAULT_SECONDARY_BOOT_MODE:zynqmp = "5"
DEFAULT_SECONDARY_BOOT_MODE:versal = "5"
DEFAULT_SECONDARY_BOOT_MODE:versal-net = "5"
DEFAULT_SECONDARY_BOOT_MODE:versal-2ve-2vm = "5"

QEMU_SECONDARY_BOOT_MODE ?= "${DEFAULT_SECONDARY_BOOT_MODE}"
QEMU_SECONDARY_BOOT_MODE[doc] = "Boot mode value for secondary boot, used for generating QB_ROOTFS_OPT values"

# ZynqMP or Versal SD and eMMC drive index.
# Set based on either primary or secondary boot modes
#
# SoC                         Device                      Drive Index
# Zynq-7000, ZynqMP, Versal   SD0                         0
# ZynqMP, Versal              SD1                         1
# ZynqMP, Versal              eMMC0(secondary boot only)  2
# ZynqMP, Versal              eMMC1                       3

QEMU_HW_SD_DRIVE_INDEX[zynq_5] = "0"

QEMU_HW_SD_DRIVE_INDEX[zynqmp_3] = "0"
QEMU_HW_SD_DRIVE_INDEX[zynqmp_5] = "1"
QEMU_HW_SD_DRIVE_INDEX[zynqmp_6] = "3"
QEMU_HW_SD_DRIVE_INDEX[zynqmp_14] = "1"

QEMU_HW_SD_DRIVE_INDEX[versal_3] = "0"
QEMU_HW_SD_DRIVE_INDEX[versal_5] = "1"
QEMU_HW_SD_DRIVE_INDEX[versal_6] = "3"
QEMU_HW_SD_DRIVE_INDEX[versal_14] = "1"

QEMU_HW_SD_DRIVE_INDEX[versal-net_3] = "0"
QEMU_HW_SD_DRIVE_INDEX[versal-net_5] = "1"
QEMU_HW_SD_DRIVE_INDEX[versal-net_6] = "3"
QEMU_HW_SD_DRIVE_INDEX[versal-net_14] = "1"

QEMU_HW_SD_DRIVE_INDEX[versal-2ve-2vm_3] = "0"
QEMU_HW_SD_DRIVE_INDEX[versal-2ve-2vm_5] = "1"
QEMU_HW_SD_DRIVE_INDEX[versal-2ve-2vm_6] = "3"
QEMU_HW_SD_DRIVE_INDEX[versal-2ve-2vm_11] = "UFS"
QEMU_HW_SD_DRIVE_INDEX[versal-2ve-2vm_14] = "1"

inherit_defer qemuboot

def qemu_rootfs_params(data, param):
    initramfs_image = data.getVar('INITRAMFS_IMAGE') or ""
    bundle_image = data.getVar('INITRAMFS_IMAGE_BUNDLE') or ""
    soc_family = data.getVar('SOC_FAMILY') or ""
    tune_features = (data.getVar('TUNE_FEATURES') or '').split()
    if 'microblaze' in tune_features:
        soc_family = 'microblaze'
    elif 'rv' in tune_features:
        soc_family = 'microblaze-v'

    if param == 'rootfs':
        return 'none' if bundle_image == "1" else ''

    elif param == 'fstype':
        fstype_dict = {
            "microblaze": "cpio.gz",
            "microblaze-v": "ext4",
            "zynq": "cpio.gz",
            "zynqmp": "cpio.gz.u-boot",
            "versal": "cpio.gz.u-boot.qemu-sd-fatimg",
            "versal-net": "cpio.gz.u-boot.qemu-sd-fatimg",
            "versal-2ve-2vm": "cpio.gz.u-boot.qemu-sd-fatimg"
        }
        if not initramfs_image:
            image_fs = (data.getVar('IMAGE_FSTYPES') or '').split()
            if 'wic.qemu-sd' in image_fs:
                return 'wic.qemu-sd'
        if soc_family not in fstype_dict:
            return ""
        return fstype_dict[soc_family]

    elif param == 'rootfs-opt':
        # Device is using a disk
        if not initramfs_image:
            # We only automatically configure either the primary or secondary media, not both

            sd_index = data.getVarFlag('QEMU_HW_SD_DRIVE_INDEX', '%s_%s' % (soc_family, data.getVar('QEMU_HW_BOOT_MODE') or ""))
            if not sd_index:
                sd_index = data.getVarFlag('QEMU_HW_SD_DRIVE_INDEX', '%s_%s' % (soc_family, data.getVar('QEMU_SECONDARY_BOOT_MODE') or ""))

            if sd_index == "UFS":
                return '-device scsi-hd,drive=d1,bus=scsi.0,channel=0,scsi-id=0,lun=0,logical_block_size=4096,physical_block_size=4096 -drive file=@ROOTFS@,if=none,id=d1,format=raw'

            if sd_index:
                return '-drive if=sd,index=%s,file=@ROOTFS@,format=raw' % (sd_index)

        # Ramdisk must be in the boot.bin or otherwise loaded by u-boot
        return ''

# Helper for setting up a machines MTD device(s)

# mtd settings:
#  zynq/zynqmp/versal - 0 - single qspi
#                       0 & 1 - striped qspi
#
#  versal/versal-net  - 4 - ospi
#
#  versal 2ve 2vm     - 0 - ospi
#
QEMU_FLASH_TYPE_DEFAULT = "undefined"
QEMU_FLASH_TYPE_DEFAULT:zynq = "${@'qspi' if d.getVar("QEMU_HW_BOOT_MODE") in [ '1' ] else 'undefined'}"
QEMU_FLASH_TYPE_DEFAULT:zynqmp = "${@'qspi' if d.getVar("QEMU_HW_BOOT_MODE") in [ '1',  '2' ] else 'undefined'}"
QEMU_FLASH_TYPE_DEFAULT:versal = "${@'qspi' if d.getVar("QEMU_HW_BOOT_MODE") in [ '1',  '2' ] else ('ospi' if d.getVar("QEMU_HW_BOOT_MODE") == '8' else 'undefined')}"
QEMU_FLASH_TYPE_DEFAULT:versal-net = "${@'qspi' if d.getVar("QEMU_HW_BOOT_MODE") in [ '1',  '2' ] else ('ospi' if d.getVar("QEMU_HW_BOOT_MODE") == '8' else 'undefined')}"
QEMU_FLASH_TYPE_DEFAULT:versal-2ve-2vm = "${@'qspi' if d.getVar("QEMU_HW_BOOT_MODE") in [ '1',  '2' ] else ('ospi' if d.getVar("QEMU_HW_BOOT_MODE") == '8' else 'undefined')}"
QEMU_FLASH_TYPE ?= "${QEMU_FLASH_TYPE_DEFAULT}"
QEMU_FLASH_TYPE[doc] = "blank/undefined, qpsi or ospi - used to determine automatic flash filename"

# Default assumes we are NOT striping the flash
QEMU_FLASH_STRIPE ?= "0"
QEMU_FLASH_STRIPE[doc] = "For qspi flashes, should the flash be striped or not"

# Default SPI file
QEMU_FLASH_FILE ??= ""
QEMU_FLASH_FILE[doc] = "Filename for the created flash file that qemu will boot"

def qemu_mtd_params(data):
    soc_family = data.getVar('SOC_FAMILY') or ""
    tune_features = (data.getVar('TUNE_FEATURES') or '').split()
    if 'microblaze' in tune_features:
        soc_family = 'microblaze'
    elif 'rv' in tune_features:
        soc_family = 'microblaze-v'

    file = data.getVar("QEMU_FLASH_FILE") or ""
    flash_type = data.getVar("QEMU_FLASH_TYPE") or "undefined"
    stripe = data.getVar("QEMU_FLASH_STRIPE") or "0"

    if not file or flash_type == "undefined":
        return ''

    if flash_type == "qspi":
        if soc_family in [ "zynq", "zynqmp", "versal", "versal-net" ]:
            if stripe != "1":
                return '-drive file=@DEPLOY_DIR_IMAGE@/%s.bin,if=mtd,format=raw,index=0' % file
            else:
                return '-drive file=@DEPLOY_DIR_IMAGE@/%s.bin_lo,if=mtd,format=raw,index=0 -drive file=@DEPLOY_DIR_IMAGE@/%s.bin_hi,if=mtd,format=raw,index=1' % (file, file)
        else:
            return 'unknown qspi configuration'
    elif flash_type == "ospi":
        if soc_family in [ "versal", "versal-net" ]:
            return '-drive file=@DEPLOY_DIR_IMAGE@/%s.bin,if=mtd,format=raw,index=4' % file
        elif soc_family in [ "versal-2ve-2vm" ]:
            return '-drive file=@DEPLOY_DIR_IMAGE@/%s.bin,if=mtd,format=raw,index=0' % file
        else:
            return 'invalid ospi configuration'

    return ''

def qemu_default_dtb(d):
    # device trees (device-tree only), these are first as they are likely desired over the kernel ones
    if "device-tree" in (d.getVar("PREFERRED_PROVIDER_virtual/dtb") or ""):
        return "system.dtb"

    # device trees (kernel only)
    if d.getVar("KERNEL_DEVICETREE"):
        dtbs = d.getVar("KERNEL_DEVICETREE").split(" ")
        dtbs = [os.path.basename(d) for d in dtbs]
        return dtbs[0]

    return ""
