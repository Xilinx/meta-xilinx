FILESEXTRAPATHS:append := ":${THISDIR}/binutils"

SRC_URI:append = " \
     file://0001-Add-wdc.ext.clear-and-wdc.ext.flush-insns.patch \
     file://0002-Add-mlittle-endian-and-mbig-endian-flags.patch \
     file://0003-Disable-the-warning-message-for-eh_frame_hdr.patch \
     file://0004-LOCAL-Fix-relaxation-of-assembler-resolved-reference.patch \
     file://0005-upstream-change-to-garbage-collection-sweep-causes-m.patch \
     file://0006-Fix-bug-in-TLSTPREL-Relocation.patch \
     file://0007-Added-Address-extension-instructions.patch \
     file://0008-fixing-the-MAX_OPCODES-to-correct-value.patch \
     file://0009-Add-new-bit-field-instructions.patch \
     file://0010-fixing-the-imm-bug.patch \
     file://0011-Patch-Microblaze-fixed-bug-in-GCC-so-that-It-will-su.patch \
     file://0012-fixing-the-constant-range-check-issue.patch \
     file://0013-Patch-Microblaze-Compiler-will-give-error-messages-i.patch \
     file://0014-Patch-MicroBlaze-initial-support-for-MicroBlaze-64-b.patch \
     file://0015-Patch-Microblaze-negl-instruction-is-overriding-rsub.patch \
     file://0016-Added-relocations-for-MB-X.patch \
     file://0017-Fixed-MB-x-relocation-issues.patch \
     file://0018-Fixing-the-branch-related-issues.patch \
     file://0019-Fixed-address-computation-issues-with-64bit-address.patch \
     file://0020-Patch-MicroBlaze-Adding-new-relocation-to-support-64.patch \
     file://0021-fixing-the-.bss-relocation-issue.patch \
     file://0022-Fixed-the-bug-in-the-R_MICROBLAZE_64_NONE-relocation.patch \
     file://0023-Revert-ld-Remove-unused-expression-state.patch \
     file://0024-fixing-the-long-long-long-mingw-toolchain-issue.patch \
     file://0025-Added-support-to-new-arithmetic-single-register-inst.patch \
     file://0026-Patch-MicroBlaze-double-imml-generation-for-64-bit-v.patch \
     file://0027-Fixed-bug-in-generation-of-IMML-instruction-for-the.patch \
     file://0028-Patch-MicroBlaze-m64-This-patch-will-remove-imml-0-a.patch \
     file://0029-Patch-MicroBlaze-improper-address-mapping-of-PROVIDE.patch \
     file://0030-Patch-microblaze-Changing-the-long-to-long-long-as-i.patch \
     file://0031-gas-revert-moving-of-md_pseudo_table-from-const.patch \
     file://0032-ld-emulparams-elf64microblaze-Fix-emulation-generati.patch \
     file://0033-Add-initial-port-of-linux-gdbserver.patch \
     file://0034-Initial-port-of-core-reading-support.patch \
     file://0035-Fix-debug-message-when-register-is-unavailable.patch \
     file://0036-microblaze-Add-build_gdbserver-yes-to-top-level-conf.patch \
     file://0037-Fixing-the-issues-related-to-GDB-7.12.patch \
     file://0038-Patch-MB-MB-binutils-Upstream-port-issues.patch \
     file://0039-Patch-MicroBlaze-Double-free-with-ld-no-keep-memory.patch \
     file://0040-Patch-MicroBlaze-Fixing-the-imm-imml-generation-for-.patch \
     file://0041-Patch-MicroBlaze-Invalid-data-offsets-pointer-after-.patch \
     "
