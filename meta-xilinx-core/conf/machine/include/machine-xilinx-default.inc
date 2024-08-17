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
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'boot.scr',     ' u-boot-xlnx-scr', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'uEnv.txt',     ' u-boot-xlnx-uenv', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'Image',        ' virtual/kernel', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'uImage',       ' virtual/kernel', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'zImage',       ' virtual/kernel', '', d)}"
WKS_FILE_DEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', '${INITRAMFS_IMAGE}-${MACHINE}.cpio.gz.u-boot', ' ${INITRAMFS_IMAGE}', '', d)}"

# Automatically add IMAGE_BOOT_FILES to /boot via packages
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'boot.bin',     ' ${PREFERRED_PROVIDER_virtual/boot-bin}', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'system.dtb',   ' ${PREFERRED_PROVIDER_virtual/dtb}', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'boot.scr',     ' u-boot-xlnx-scr', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'uEnv.txt',     ' u-boot-xlnx-uenv', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'Image',        ' kernel-image-image', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'uImage',       ' kernel-image-uimage', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', 'zImage',       ' kernel-image-zimage', '', d)}"
MACHINE_ESSENTIAL_EXTRA_RDEPENDS .= "${@bb.utils.contains('IMAGE_BOOT_FILES_INSTALLED', '${INITRAMFS_IMAGE}-${MACHINE}.cpio.gz.u-boot', ' initramdisk-${INITRAMFS_IMAGE}', '', d)}"

# Default SD card wks file, split /boot and /
WKS_FILES ?= "xilinx-default-sd.wks"

IMAGE_BOOT_FILES ?= "${@get_default_image_boot_files(d)}"

def get_default_image_boot_files(d):
    files = []

    esw_type = d.getVar('XILINX_WITH_ESW') or ''
    if esw_type:
        if 'zynq' in d.getVar('OVERRIDES').split(':'):
            files.append('boot.bin')
        if 'zynqmp' in d.getVar('OVERRIDES').split(':'):
            files.append('boot.bin')
        if 'versal' in d.getVar('OVERRIDES').split(':'):
            files.append('boot.bin')
        if 'versal-net' in d.getVar('OVERRIDES').split(':'):
            files.append('boot.bin')

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
        files.append("system.dtb")
        files.append("devicetree/*.dtb;devicetree/")
        files.append("devicetree/*.dtbo;devicetree/")

    # device trees (kernel only)
    if d.getVar("KERNEL_DEVICETREE"):
        dtbs = d.getVar("KERNEL_DEVICETREE").split(" ")
        dtbs = [os.path.basename(d) for d in dtbs]
        for dtb in dtbs:
            files.append(dtb)

    return " ".join(files)

EXTRA_IMAGEDEPENDS += "${@get_default_extra_imagedepends(d)}"

def get_default_extra_imagedepends(d):
    depends = []

    # Add firmware dependencies
    esw_type = d.getVar('XILINX_WITH_ESW') or ''
    if esw_type:
        depends.append('libyaml-native')
        depends.append('python3-cython-native')
        depends.append('python3-pyyaml-native')
        if 'microblaze' in d.getVar('OVERRIDES').split(':'):
            depends.append('virtual/bitstream')
        if 'zynq' in d.getVar('OVERRIDES').split(':'):
            depends.append('virtual/boot-bin')
        if 'zynqmp' in d.getVar('OVERRIDES').split(':'):
            depends.append('virtual/boot-bin')
        if 'versal' in d.getVar('OVERRIDES').split(':'):
            depends.append('virtual/boot-bin')
            depends.append('virtual/cdo')
        if 'versal-net' in d.getVar('OVERRIDES').split(':'):
            depends.append('virtual/boot-bin')
            depends.append('virtual/cdo')

    return " ".join(depends)



XSERVER_EXT ?= ""

FPGA_MNGR_RECONFIG_ENABLE ?= "1"

# This variable is supported only with SysVinit.
SERIAL_CONSOLES_CHECK = "${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', '${SERIAL_CONSOLES}', '', d)}"
