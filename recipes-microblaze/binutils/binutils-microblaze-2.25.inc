
# Add MicroBlaze Patches
FILESEXTRAPATHS_append := "${THISDIR}/files:"
SRC_URI_append_microblaze += " \
		file://Add-wdc.ext.clear-and-wdc.ext.flush.patch \
		file://Add-mlittle-endian-and-mbig-endian.patch \
		file://Disable-the-warning-message-for-eh_frame_hdr.patch \
		file://Fix-relaxation-of-assembler-resolved-reference.patch \
		file://Fixup-debug_loc-sections-after-linker-relaxati.patch \
		file://upstream-change-to-garbage-collection-s.patch \
		"
