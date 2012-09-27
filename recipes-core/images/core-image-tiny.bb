DESCRIPTION = "A tiny image just capable of allowing a device to boot."

IMAGE_INSTALL = "base-files \
    base-passwd \
    busybox \
    busybox-mdev \
    initscripts \
    modutils-initscripts \
    netbase \
    tinylogin \
    sysvinit \
    ${MACHINE_ESSENTIAL_EXTRA_RDEPENDS} \
    ${ROOTFS_PKGMANAGE_BOOTSTRAP} \
    ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE = "8192"

# remove not needed ipkg informations
ROOTFS_POSTPROCESS_COMMAND += "remove_packaging_data_files ; "
