INHERIT += "xilinx-vars"
INHERIT += "xilinx-deprecated"

# Default Xilinx BSP Machine settings

MACHINE_FEATURES_BACKFILL_CONSIDERED += "rtc"

# File System Configuration
IMAGE_FSTYPES ?= "tar.gz cpio cpio.gz cpio.gz.u-boot"

# Kernel Configuration
PREFERRED_PROVIDER_virtual/kernel ??= "linux-xlnx"

# Device tree Configuration
PREFERRED_PROVIDER_virtual/dtb ??= "${@'device-tree' if d.getVar('XILINX_WITH_ESW') else ''}"

# U-Boot Configuration
PREFERRED_PROVIDER_virtual/bootloader ??= "u-boot-xlnx"
PREFERRED_PROVIDER_virtual/boot-bin ??= "xilinx-bootbin"
PREFERRED_PROVIDER_u-boot-tools ??= "u-boot-tools-xlnx"
PREFERRED_PROVIDER_u-boot-tools-native ??= "u-boot-tools-xlnx-native"
PREFERRED_PROVIDER_nativesdk-u-boot-tools ??= "nativesdk-u-boot-tools-xlnx"

# Libmetal and OpenAMP Configuration
PREFERRED_PROVIDER_libmetal ?= "libmetal-xlnx"
PREFERRED_PROVIDER_open-amp ?= "open-amp-xlnx"

UBOOT_SUFFIX ?= "bin"

UBOOT_BINARY ?= "u-boot.${UBOOT_SUFFIX}"
UBOOT_ELF ?= "u-boot.elf"

XSERVER ?= " \
	xserver-xorg \
	xf86-input-evdev \
	xf86-input-mouse \
	xf86-video-fbdev \
	${XSERVER_EXT} \
	"

SYSTEM_DTFILE ??= ""
CONFIG_DTFILE ??= "${SYSTEM_DTFILE}"
FIT_CONF_DEFAULT_DTB ?= "${@os.path.basename(d.getVar('CONFIG_DTFILE')).replace('.dts', '.dtb') if d.getVar('CONFIG_DTFILE') else ''}"

# Define to avoid parsse errors below if undefind elsewhere
INITRAMFS_IMAGE ??= ""

# IMAGE_BOOT_FILES is a whitespace-separated list of entries specifying files to be installed into the boot partition
# entries can change the installed filename by specifying the destination name after a semicolon (e.g. u-boot.img;uboot)
# make sure to use the installed filename when scanning IMAGE_BOOT_FILES
IMAGE_BOOT_FILES_INSTALLED = "${@' '.join([x.split(';')[-1] for x in d.getVar('IMAGE_BOOT_FILES').split()])}"

# Automatically add WKS_FILE_DEPENDS based on the configuration
# Initial value from oe-core/meta/classes-recipe/image_types_wic.bbclass
WKS_FILE_DEPENDS ?= "${WKS_FILE_DEPENDS_DEFAULT} ${WKS_FILE_DEPENDS_BOOTLOADERS}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'boot.bin',     ' virtual/boot-bin', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'system.dtb',   ' virtual/dtb', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'boot.scr',     ' ${UBOOT_BOOT_SCRIPT}', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'uEnv.txt',     ' u-boot-xlnx-uenv', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', d.getVar('KERNEL_IMAGETYPE') or 'Image',        ' virtual/kernel', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', '${INITRAMFS_IMAGE}-${MACHINE}.cpio.gz.u-boot', ' ${INITRAMFS_IMAGE}', '', d)}"

# Automatically add IMAGE_BOOT_FILES to /boot via packages
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'boot.bin',     ' ${PREFERRED_PROVIDER_virtual/boot-bin}', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'system.dtb',   ' ${PREFERRED_PROVIDER_virtual/dtb}', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'boot.scr',     ' ${UBOOT_BOOT_SCRIPT}', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'uEnv.txt',     ' u-boot-xlnx-uenv', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', d.getVar('KERNEL_IMAGETYPE') or 'Image',        ' kernel-image-' + d.getVar('KERNEL_IMAGETYPE').lower(), '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', '${INITRAMFS_IMAGE}-${MACHINE}.cpio.gz.u-boot', ' initramdisk-${INITRAMFS_IMAGE}', '', d)}"

# xilinx-default-sd.wks - u-boot/boot.scr based
# xilinx-default-sd-efi.wks - u-boot/EFI based
WKS_FILE ?= "${@bb.utils.contains('DISTRO_FEATURES', 'efi', 'xilinx-default-sd-efi.wks', 'xilinx-default-sd.wks', d)}"

# Set the EFI_PROVIDER only if EFI is enabled in the distro
EFI_PROVIDER ??= "${@bb.utils.contains('DISTRO_FEATURES', 'efi', bb.utils.contains("DISTRO_FEATURES", "systemd", "systemd-boot", "grub-efi", d), '', d)}"
WICVARS:append = "${@bb.utils.contains('DISTRO_FEATURES', 'efi', ' EFI_PROVIDER', '', d)}"
WICUFSVARS:append = "${@bb.utils.contains('DISTRO_FEATURES', 'efi', ' EFI_PROVIDER', '', d)}"

# Transfer the DEFAULT_HW_BOOT_MODE to HW_BOOT_MODE
# This indirection allows for an image to override it
DEFAULT_HW_BOOT_MODE ??= "5"
HW_BOOT_MODE ?= "${DEFAULT_HW_BOOT_MODE}"
HW_BOOT_MODE[doc] = "The hardware boot mode as defined by the SoC TRM.  This should be set by the machine.conf, but the user or image recipe can override as necessary."

HW_BOOT_MODE_TYPE = "${@get_verbose_boot_mode(d)}"
HW_BOOT_MODE_TYPE[docs] = "Verbose description of the boot mode intended to be used for debugging and log message.  The values should not be relied on as they may change."

BUILDCFG_VARS:append = " HW_BOOT_MODE HW_BOOT_MODE_TYPE SOC_ON_DISK_BOOT_BIN"

def get_verbose_boot_mode(d):
    hw_boot_mode = d.getVar('HW_BOOT_MODE')
    soc_boot_mode_mapping = d.getVar('SOC_BOOT_MODE_MAPPING')
    if not hw_boot_mode or not soc_boot_mode_mapping:
        return ''

    # Parse the mapping string into a dictionary
    mapping = dict(entry.split(":", 1) for entry in soc_boot_mode_mapping.split())

    if hw_boot_mode in mapping:
        return mapping[hw_boot_mode]
    return ''

# Does this configuration require a boot.bin on the image?
SOC_ON_DISK_BOOT_BIN ?= "${@'1' if (d.getVar('HW_BOOT_MODE') or '') in (d.getVar('SOC_DISK_BOOT_MODE_MAPPING') or '').split() and d.getVar('XILINX_WITH_ESW') else ''}"
SOC_ON_DISK_BOOT_BIN[doc] = "When set to 1, the boot.bin should be added to the boot media."

# This is generally distro boot and processed via bootimg-partition
IMAGE_BOOT_FILES ?= "${@'boot.bin ' if d.getVar('SOC_ON_DISK_BOOT_BIN') == '1' else ''}${KERNEL_IMAGETYPE}"

# This is EFI boot bootimg-efi(-amd)
IMAGE_EFI_BOOT_FILES ?= "${@'boot.bin' if d.getVar('SOC_ON_DISK_BOOT_BIN') == '1' else ''}"

# These dependencies are SOC specific, and only apply if building with
# XSCT or SDT modes
SOC_EXTRA_IMAGEDEPENDS[microblaze] = "virtual/bitstream"
SOC_EXTRA_IMAGEDEPENDS[microblaze-v] = ""
SOC_EXTRA_IMAGEDEPENDS[zynq] = "virtual/boot-bin"
SOC_EXTRA_IMAGEDEPENDS[zynqmp] = "virtual/boot-bin"
SOC_EXTRA_IMAGEDEPENDS[versal] = "virtual/boot-bin virtual/cdo"
SOC_EXTRA_IMAGEDEPENDS[versal-net] = "virtual/boot-bin virtual/cdo"
SOC_EXTRA_IMAGEDEPENDS[versal-2ve-2vm] = "virtual/boot-bin virtual/cdo"
SOC_EXTRA_IMAGEDEPENDS = "${@d.getVarFlag('SOC_EXTRA_IMAGEDEPENDS', d.getVar('SOC_FAMILY') or '') or '' if d.getVar('XILINX_WITH_ESW') else ''}"

EXTRA_IMAGEDEPENDS += "${SOC_EXTRA_IMAGEDEPENDS}"

XSERVER_EXT ?= ""

FPGA_MNGR_RECONFIG_ENABLE ?= "1"

# This variable is supported only with SysVinit.
SERIAL_CONSOLES_CHECK = "${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', '${SERIAL_CONSOLES}', '', d)}"
