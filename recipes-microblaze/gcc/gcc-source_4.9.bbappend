
# Add MicroBlaze Patches
FILESEXTRAPATHS_append := "${THISDIR}/files:"
SRC_URI_append = " \
		file://microblaze-musl-support.patch \
		"

