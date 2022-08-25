INHERIT += "xilinx-deprecated"

# Default Xilinx BSP Machine settings

MACHINE_FEATURES_BACKFILL_CONSIDERED += "rtc"

# File System Configuration
IMAGE_FSTYPES ?= "tar.gz cpio cpio.gz cpio.gz.u-boot"

# Kernel Configuration
PREFERRED_PROVIDER_virtual/kernel ??= "linux-xlnx"

# U-Boot Configuration
PREFERRED_PROVIDER_virtual/bootloader ??= "u-boot-xlnx"
PREFERRED_PROVIDER_virtual/boot-bin ??= "xilinx-bootbin"

do_image_wic[depends] += "${@' '.join('%s:do_deploy' % r for r in (d.getVar('WIC_DEPENDS') or "").split())}"

UBOOT_SUFFIX ?= "bin"

UBOOT_BINARY ?= "u-boot.${UBOOT_SUFFIX}"
UBOOT_ELF ?= "u-boot"
UBOOT_ELF:aarch64 ?= "u-boot.elf"

#Hardware accelaration
PREFERRED_PROVIDER_virtual/libgles1:mali400 = "libmali-xlnx"
PREFERRED_PROVIDER_virtual/libgles2:mali400 = "libmali-xlnx"
PREFERRED_PROVIDER_virtual/egl:mali400 = "libmali-xlnx"
PREFERRED_PROVIDER_virtual/libgl:mali400 = "mesa-gl"
PREFERRED_PROVIDER_virtual/mesa:mali400 = "mesa-gl"

XSERVER ?= " \
	xserver-xorg \
	xf86-input-evdev \
	xf86-input-mouse \
	xf86-input-keyboard \
	xf86-video-fbdev \
	${XSERVER_EXT} \
	"

# Automatically add WKS_FILE_DEPENDS based on the configuration
WKS_FILE_DEPENDS:append = "${@bb.utils.contains('IMAGE_BOOT_FILES', 'boot.bin', ' xilinx-bootbin', '', d)}"
WKS_FILE_DEPENDS:append = "${@bb.utils.contains('IMAGE_BOOT_FILES', 'system.dtb', ' virtual/dtb', '', d)}"
WKS_FILE_DEPENDS:append = "${@bb.utils.contains('IMAGE_BOOT_FILES', 'boot.scr', ' u-boot-zynq-scr', '', d)}"
WKS_FILE_DEPENDS:append = "${@bb.utils.contains('IMAGE_BOOT_FILES', 'uEnv.txt', ' u-boot-zynq-uenv', '', d)}"
WKS_FILE_DEPENDS:append = "${@bb.utils.contains('IMAGE_BOOT_FILES', 'atf-uboot.ub', ' arm-trusted-firmware', '', d)}"

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
    if "device-tree" in (d.getVar("MACHINE_ESSENTIAL_EXTRA_RDEPENDS") or ""):
        files.append("devicetree/*.dtb")


    # device trees (kernel only)
    if d.getVar("KERNEL_DEVICETREE"):
        dtbs = d.getVar("KERNEL_DEVICETREE").split(" ")
        dtbs = [os.path.basename(d) for d in dtbs]
        for dtb in dtbs:
            files.append(dtb)

    return " ".join(files)

XSERVER_EXT ?= ""

FPGA_MNGR_RECONFIG_ENABLE ?= "1"
