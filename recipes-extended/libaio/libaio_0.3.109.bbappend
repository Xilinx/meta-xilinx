
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Apply patch to add "microblaze" support to libaio
SRC_URI += "file://microblaze.patch"

