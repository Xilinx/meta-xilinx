SUMMARY = "A nearly empty image just capable of allowing a device to \
boot via boot.bin."
DESCRIPTION = "Builds a deliberately empty Yocto image so that QEMU \
and hardware boot flows can validate firmware/boot.bin images without \
requiring a populated rootfs."

# Ensure this is really empty
IMAGE_INSTALL = ""
PACKAGE_INSTALL = ""

# Disable various files
EXTRA_USERS_PARAMS = ""
EXTRA_USERS_SUDOERS = ""

IMAGE_LINGUAS = " "

LICENSE = "MIT"

IMAGE_FEATURES = ""

# The only thing should be the boot.bin, and only if SOC_ON_DISK_BOOT_BIN is 1.
IMAGE_BOOT_FILES = "${@'boot.bin ' if d.getVar('SOC_ON_DISK_BOOT_BIN') == '1' else ''}"

PACKAGE_EXCLUDE_COMPLEMENTARY = ""
MACHINE_HWCODECS = ""

inherit image

WKS_FILE = "xilinx-empty-sd.wks"
# Override WKS_FILES to prevent local.conf from substituting a different .wks
# (e.g. Xen builds set WKS_FILES globally to xilinx-default-sd.wks)
WKS_FILES = "${WKS_FILE}"

IMAGE_ROOTFS_SIZE ?= "8192"

# Avoid circular dependencies
EXTRA_IMAGEDEPENDS = "virtual/boot-bin"
