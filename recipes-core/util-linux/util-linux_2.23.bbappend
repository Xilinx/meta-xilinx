FILESEXTRAPATHS := "${THISDIR}/files"

# Apply patch to add "microblaze" support to util-linux
SRC_URI += "file://fdisk-add-support-for-the-MicroBlaze-architecture.patch"

