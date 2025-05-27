FILESEXTRAPATHS:prepend:imgrcvry := "${THISDIR}/${PN}:"

SRC_URI:append:imgrcvry = " file://amd_aarch64_mini_defconfig"

# Unsetting the KBUILD_DEFCONFIG so that kernel-yocto.bbclass will use the
# defconfig from SRC_URI for imgrcvry distro
KBUILD_DEFCONFIG:imgrcvry ?= ""

