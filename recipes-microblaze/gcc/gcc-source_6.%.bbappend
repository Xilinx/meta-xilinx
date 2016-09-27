
# Add MicroBlaze Patches
FILESEXTRAPATHS_append := "${THISDIR}/files:"
SRC_URI_append = " \
		file://microblaze.md-Improve-adddi3-and-subdi3-insn-definit.patch \
		file://gcc-config-microblaze-Make-r21-fixed-due-to-Linux-AB.patch \
		"

