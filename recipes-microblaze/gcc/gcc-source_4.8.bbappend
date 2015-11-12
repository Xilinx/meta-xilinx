
# Add MicroBlaze Patches
FILESEXTRAPATHS_append := "${THISDIR}/files:"
SRC_URI_append += " \
		file://0001-Patch-microblaze-Enable-DWARF-exception-handling-sup.patch \
		file://0002-Patch-microblaze-Add-4-byte-implementation-for-atomi.patch \
		file://0003-Patch-microblaze-Extend-jump-insn-to-accept-bri-to-S.patch \
		file://0005-Patch-microblaze-Add-fstack-usage-support.patch \
		file://gcc-Cherry-pick-mainline-patch-to-resolve-MB-k.patch \
		"

