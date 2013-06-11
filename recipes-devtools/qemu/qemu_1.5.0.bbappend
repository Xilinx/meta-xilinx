QEMU_TARGETS += "microblazeel"

FILESEXTRAPATHS_append := "${THISDIR}/files:"
SRC_URI_append += " \
		file://microblaze-Add-support-for-loading-initrd-images.patch \
		"
