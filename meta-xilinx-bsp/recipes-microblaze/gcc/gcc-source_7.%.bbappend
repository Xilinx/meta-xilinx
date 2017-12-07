# Add MicroBlaze Patches (only when using MicroBlaze)
FILESEXTRAPATHS_append_microblaze := "${THISDIR}/gcc-7:"
SRC_URI_append_microblaze = " \
		file://0001-Revert.patch \
		file://0002-microblaze.md-Improve-adddi3-and-subdi3-insn-definit.patch \
		file://0003-microblaze-sync.md-Correct-behaviour-and-define-side.patch \
		file://0004-gcc-config-microblaze-Use-default-ident-output-gener.patch \
		"

