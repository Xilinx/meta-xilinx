FILESEXTRAPATHS := "${THISDIR}/files"

# Apply patch to add "microblaze" support to util-linux
SRC_URI += "file://microblaze-fixes.patch"

