
# MicroBlaze does not support LTTng UST
LTTNGUST_microblaze = ""

# Add MicroBlaze Patches
FILESEXTRAPATHS_append := "${THISDIR}/files:"
SRC_URI_append += " \
		file://0001-Add-initial-port-of-linux-gdbserver.patch \
		file://0002-Initial-port-of-core-reading-support.patch \
		file://0003-Initial-support-for-native-gdb.patch \
		file://0004-Robustify-inline-function-support.patch \
		file://0005-Fix-debug-message-when-register-is-unavailable.patch \
		file://0006-Add-slr-and-shr-regs-and-little-endian-breakpoint.patch \
		file://0007-Fix-relaxation-of-assembler-resolved-references.patch \
		file://0008-Fixup-debug_loc-sections-after-linker-relaxation.patch \
		file://0009-Add-wdc.ext.clear-and-wdc.ext.flush-insns.patch \
		file://0010-revert-master-rebase-changes-to-gdbserver.patch \
		file://0011-Patch-microblaze-Add-mb-singlestepping.patch \
		file://0012-Added-Backtrace-support-to-GDB.patch \
		file://0013-microblaze-Handle-framless-functions-in-determining-.patch \
		file://0014-microblaze-Add-build_gdbserver-yes-to-top-level-conf.patch \
		"
