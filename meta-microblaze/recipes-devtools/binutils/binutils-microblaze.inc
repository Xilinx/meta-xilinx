FILESEXTRAPATHS:append := ":${THISDIR}/binutils"

SRC_URI:append = " \
     file://0001-Add-wdc.ext.clear-and-wdc.ext.flush-insns.patch \
     file://0002-Add-mlittle-endian-and-mbig-endian-flags.patch \
     file://0003-Disable-the-warning-message-for-eh_frame_hdr.patch \
     file://0004-Fix-relaxation-of-assembler-resolved-references-Fixu.patch \
     file://0005-upstream-change-to-garbage-collection-sweep-causes-m.patch \
     file://0006-Fix-bug-in-TLSTPREL-Relocation.patch \
     file://0007-Added-Address-extension-instructions.patch \
     file://0008-fixing-the-MAX_OPCODES-to-correct-value.patch \
     file://0009-Add-new-bit-field-instructions.patch \
     file://0010-fixing-the-imm-bug.-with-relax-option-imm-1-is-also-.patch \
     file://0011-fixed-bug-in-GCC-so-that-It-will-support-.long-0U-an.patch \
     file://0012-fixing-the-constant-range-check-issue-sample-error-n.patch \
     file://0013-Compiler-will-give-error-messages-in-more-detail-for.patch \
     file://0014-initial-support-for-MicroBlaze-64-bit-m64.patch \
     file://0015-negl-instruction-is-overriding-rsubl-fixed-it-by-cha.patch \
     file://0016-Added-relocations-for-MB-X.patch \
     file://0017-Fixed-MB-x-relocation-issues-Added-imml-for-required.patch \
     file://0018-Fixing-the-branch-related-issues.patch \
     file://0019-Fixed-address-computation-issues-with-64bit-address-.patch \
     file://0020-Patch-MicroBlaze-Adding-new-relocation-to-support-64.patch \
     file://0021-fixing-the-.bss-relocation-issue.patch \
     file://0022-Fixed-the-bug-in-the-R_MICROBLAZE_64_NONE-relocation.patch \
     file://0023-Revert-ld-Remove-unused-expression-state-defsym-symb.patch \
     file://0024-fixing-the-long-long-long-mingw-toolchain-issue.patch \
     file://0025-Added-support-to-new-arithmetic-single-register-inst.patch \
     file://0026-double-imml-generation-for-64-bit-values.patch \
     file://0027-Fixed-bug-in-generation-of-IMML-instruction-for-the.patch \
     file://0028-This-patch-will-remove-imml-0-and-imml-1-instruction.patch \
     file://0029-improper-address-mapping-of-PROVIDE-directive-symbol.patch \
     file://0030-Changing-the-long-to-long-long-as-in-Windows-long-is.patch \
     file://0031-gas-revert-moving-of-md_pseudo_table-from-const.patch \
     file://0032-ld-emulparams-elf64microblaze-Fix-emulation-generati.patch \
     file://0033-Add-initial-port-of-linux-gdbserver-add-gdb_proc_ser.patch \
     file://0034-Initial-port-of-core-reading-support-Added-support-f.patch \
     file://0035-MB-binutils-Upstream-port-issues.patch \
     file://0036-Double-free-with-ld-no-keep-memory.-Proposed-patches.patch \
     file://0037-Fixing-the-imm-imml-generation-for-16-bit-argument-C.patch \
     file://0038-Invalid-data-offsets-pointer-after-relaxation.-Propo.patch \
     "
