# HDF_MACHINE should be set _AFTER_ this has been loaded
HDF_MACHINE_PRIOR := "${HDF_MACHINE}"
INHERIT += "xilinx-vars"
INHERIT += "xilinx-deprecated"

# Default Xilinx BSP Machine settings

MACHINE_FEATURES_BACKFILL_CONSIDERED += "rtc"

# File System Configuration
IMAGE_FSTYPES ?= "tar.gz cpio cpio.gz cpio.gz.u-boot"

# Kernel Configuration
PREFERRED_PROVIDER_virtual/kernel ??= "linux-xlnx"

# Device tree Configuration
PREFERRED_PROVIDER_virtual/dtb ??= "device-tree"

# U-Boot Configuration
PREFERRED_PROVIDER_virtual/bootloader ??= "u-boot-xlnx"
PREFERRED_PROVIDER_virtual/boot-bin ??= "xilinx-bootbin"
PREFERRED_PROVIDER_u-boot-tools ??= "u-boot-tools-xlnx"
PREFERRED_PROVIDER_u-boot-tools-native ??= "u-boot-tools-xlnx-native"
PREFERRED_PROVIDER_nativesdk-u-boot-tools ??= "nativesdk-u-boot-tools-xlnx"

do_image_wic[depends] += "${@' '.join('%s:do_deploy' % r for r in (d.getVar('WIC_DEPENDS') or "").split())}"

UBOOT_SUFFIX ?= "bin"

UBOOT_BINARY ?= "u-boot.${UBOOT_SUFFIX}"
UBOOT_ELF ?= "u-boot"
UBOOT_ELF:aarch64 ?= "u-boot.elf"

# libmali is selected by DISTRO_FEATURE of libmali & MACHINE_FEATURES of mali400
# lima is selected by DISTRO_FEATURE != libmali & MACHINE_FEATURES of mali400
# default mesa because otherwise
def xlnx_is_libmali_enabled(d):
    if bb.utils.contains('MACHINE_FEATURES', 'mali400', '1', '', d) == '1':
        if bb.utils.contains('DISTRO_FEATURES', 'libmali', '1', '', d) == '1':
            return True
    return False

# libmali requires certain preferred providers to be selected
PREFERRED_PROVIDER_virtual/egl ?= "${@'libmali-xlnx' if xlnx_is_libmali_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/libgl ?= "${@'mesa-gl' if xlnx_is_libmali_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/libgles1 ?= "${@'libmali-xlnx' if xlnx_is_libmali_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/libgles2 ?= "${@'libmali-xlnx' if xlnx_is_libmali_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/libgles3 ?= "${@'NOT_SUPPORTED' if xlnx_is_libmali_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/libgbm ?= "${@'libmali-xlnx' if xlnx_is_libmali_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/mesa ?= "${@'mesa-gl' if xlnx_is_libmali_enabled(d) else 'mesa'}"

XSERVER ?= " \
	xserver-xorg \
	xf86-input-evdev \
	xf86-input-mouse \
	xf86-video-fbdev \
	${XSERVER_EXT} \
	"

SYSTEM_DTFILE ??= ""
CONFIG_DTFILE ??= "${SYSTEM_DTFILE}"
FIT_CONF_DEFAULT_DTB ?= "${@os.path.basename(d.getVar('CONFIG_DTFILE')).replace('.dts', '.dtb') if d.getVar('CONFIG_DTFILE') else 'system.dtb'}"

# Define to avoid parsse errors below if undefind elsewhere
INITRAMFS_IMAGE ??= ""

# Automatically add WKS_FILE_DEPENDS based on the configuration
# Initial value from oe-core/meta/classes-recipe/image_types_wic.bbclass
WKS_FILE_DEPENDS ?= "${WKS_FILE_DEPENDS_DEFAULT} ${WKS_FILE_DEPENDS_BOOTLOADERS}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'boot.bin',     ' virtual/boot-bin', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'system.dtb',   ' virtual/dtb', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'boot.scr',     ' u-boot-xlnx-scr', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'uEnv.txt',     ' u-boot-xlnx-uenv', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'atf-uboot.ub', ' arm-trusted-firmware', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'Image',        ' virtual/kernel', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'uImage',       ' virtual/kernel', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'zImage',       ' virtual/kernel', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'u-boot.bin',   ' virtual/bootloader', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'u-boot.elf',   ' virtual/bootloader', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', '${INITRAMFS_IMAGE}-${MACHINE}.cpio.gz.u-boot', ' ${INITRAMFS_IMAGE}', '', d)}"

# Automatically add IMAGE_BOOT_FILES to /boot via packages
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'boot.bin',     ' ${PREFERRED_PROVIDER_virtual/boot-bin}', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'system.dtb',   ' ${PREFERRED_PROVIDER_virtual/dtb}', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'boot.scr',     ' u-boot-xlnx-scr', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'uEnv.txt',     ' u-boot-xlnx-uenv', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'atf-uboot.ub', ' arm-trusted-firmware', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'u-boot.bin',   ' ${PREFERRED_PROVIDER_virtual/bootloader}-bin', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'u-boot.elf',   ' ${PREFERRED_PROVIDER_virtual/bootloader}-elf', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'Image',        ' kernel-image-image', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'uImage',       ' kernel-image-uimage', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', 'zImage',       ' kernel-image-zimage', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES', '${INITRAMFS_IMAGE}-${MACHINE}.cpio.gz.u-boot', ' initramdisk-${INITRAMFS_IMAGE}', '', d)}"

# Default SD card wks file, split /boot and /
WKS_FILES ?= "xilinx-default-sd.wks"

IMAGE_BOOT_FILES ?= "${@get_default_image_boot_files(d)}"

def get_default_image_boot_files(d):
    files = []

    # kernel images
    kerneltypes = set((d.getVar("KERNEL_IMAGETYPE") or "").split())
    kerneltypes |= set((d.getVar("KERNEL_IMAGETYPES") or "").split())
    kerneltypes |= set((d.getVar("KERNEL_ALT_IMAGETYPE") or "").split())
    for i in kerneltypes:
        files.append(i)

    # u-boot image
    if d.getVar("UBOOT_BINARY"):
        files.append(d.getVar("UBOOT_BINARY"))

    # device trees (device-tree only), these are first as they are likely desired over the kernel ones
    if "device-tree" in (d.getVar("PREFERRED_PROVIDER_virtual/dtb") or ""):
        files.append("devicetree/*.dtb;devicetree/")
        files.append("devicetree/*.dtbo;devicetree/")

    # device trees (kernel only)
    if d.getVar("KERNEL_DEVICETREE"):
        dtbs = d.getVar("KERNEL_DEVICETREE").split(" ")
        dtbs = [os.path.basename(d) for d in dtbs]
        for dtb in dtbs:
            files.append(dtb)

    return " ".join(files)

XSERVER_EXT ?= ""

FPGA_MNGR_RECONFIG_ENABLE ?= "1"
