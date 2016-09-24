
# Add MicroBlaze Patches
FILESEXTRAPATHS_append := "${THISDIR}/files:"
SRC_URI_append_microblaze = " \
		file://upstream-change-to-garbage-collection-s.patch \
		file://bfd-elf32-microblaze.c-Fix-regression-with-RELA.patch \
		"

