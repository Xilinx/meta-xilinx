INITRAMFS_IMAGE ??= ""

# Adjust our name to be explicit to what we're going to produce
PN = "initramdisk-${INITRAMFS_IMAGE}"

DESCRIPTION = "Provide the initramdisk available via a package, installed in /boot"

DEPENDS = "${INITRAMFS_IMAGE}"

LICENSE = "MIT"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install[vardepsexclude] += "DEPLOY_DIR_IMAGE"
do_install[depends] += "${INITRAMFS_IMAGE}:do_image_complete"
do_install[cleandirs] = "${D}"
do_install() {
	if [ -e ${DEPLOY_DIR_IMAGE}/${INITRAMFS_IMAGE}-${MACHINE}.cpio.gz.u-boot ]; then
		install -d ${D}/boot/
		install -m 0644 ${DEPLOY_DIR_IMAGE}/${INITRAMFS_IMAGE}-${MACHINE}.cpio.gz.u-boot ${D}/boot/.
	else
		bbfatal "Unable to find expected initramfs: ${INITRAMFS_IMAGE}-${MACHINE}.cpio.gz.u-boot"
	fi
}

PACKAGES = "${PACKAGE_BEFORE_PN} ${PN}"

RPROVIDES:${PN} = "initramdisk"
FILES:${PN} = "/boot/${INITRAMFS_IMAGE}-${MACHINE}.cpio.gz.u-boot"

python() {
    if not d.getVar('INITRAMFS_IMAGE'):
        bb.parse.SkipRecipe("No init ramdisk enabled. This package requires INITRAMFS_IMAGE to be defined.")
}
