FILESEXTRAPATHS:append := ":${THISDIR}/binutils"

SRC_URI:append = " \
     file://0001-Add-wdc.ext.clear-and-wdc.ext.flush-insns.patch \
     file://0002-Add-mlittle-endian-and-mbig-endian-flags.patch \
     file://0003-Disable-the-warning-message-for-eh_frame_hdr.patch \
     file://0004-LOCAL-Fix-relaxation-of-assembler-resolved-reference.patch \
     file://0005-upstream-change-to-garbage-collection-sweep-causes-m.patch \
     file://0006-Fix-bug-in-TLSTPREL-Relocation.patch \
     file://0007-Added-Address-extension-instructions.patch \
     file://0008-Add-new-bit-field-instructions.patch \
     file://0009-Patch-Microblaze-fixed-bug-in-GCC-so-that-It-will-su.patch \
     file://0010-fixing-the-constant-range-check-issue.patch \
     file://0011-Patch-Microblaze-Compiler-will-give-error-messages-i.patch \
     file://0012-Patch-MicroBlaze-initial-support-for-MicroBlaze-64-b.patch \
     file://0013-Patch-Microblaze-negl-instruction-is-overriding-rsub.patch \
     file://0014-Added-relocations-for-MB-X.patch \
     file://0015-Fixed-MB-x-relocation-issues.patch \
     file://0016-Fixing-the-branch-related-issues.patch \
     file://0017-Fixed-address-computation-issues-with-64bit-address.patch \
     file://0018-Patch-MicroBlaze-Adding-new-relocation-to-support-64.patch \
     file://0019-fixing-the-.bss-relocation-issue.patch \
     file://0020-Fixed-the-bug-in-the-R_MICROBLAZE_64_NONE-relocation.patch \
     file://0021-Revert-ld-Remove-unused-expression-state.patch \
     file://0022-fixing-the-long-long-long-mingw-toolchain-issue.patch \
     file://0023-Added-support-to-new-arithmetic-single-register-inst.patch \
     file://0024-Patch-MicroBlaze-double-imml-generation-for-64-bit-v.patch \
     file://0025-Fixed-bug-in-generation-of-IMML-instruction-for-the.patch \
     file://0026-Patch-MicroBlaze-m64-This-patch-will-remove-imml-0-a.patch \
     file://0027-Patch-MicroBlaze-improper-address-mapping-of-PROVIDE.patch \
     file://0028-Patch-microblaze-Changing-the-long-to-long-long-as-i.patch \
     file://0029-gas-revert-moving-of-md_pseudo_table-from-const.patch \
     file://0030-ld-emulparams-elf64microblaze-Fix-emulation-generati.patch \
     file://0031-Patch-MicroBlaze-Invalid-data-offsets-pointer-after-.patch \
     file://0032-Patch-MicroBlaze-Double-free-with-ld-no-keep-memory.patch \
     file://0033-Patch-MB-MB-binutils-Upstream-port-issues.patch \
     file://0034-Patch-MicroBlaze-By-default-the-linker-will-generate.patch \
     "

LDGOLD_ALTS:microblaze = ""
USE_ALTERNATIVES_FOR:remove:microblaze = "gprof"
