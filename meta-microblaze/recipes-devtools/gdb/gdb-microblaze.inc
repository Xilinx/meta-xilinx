# MicroBlaze does not support LTTng UST
LTTNGUST:microblaze = ""

# Add MicroBlaze patches
FILESEXTRAPATHS:append := ":${THISDIR}/gdb"

# Our changes are all local, no real patch-status
ERROR_QA:remove = "patch-status"

LDFLAGS:append:class-target:microblaze = " -latomic"

SRC_URI:append:microblaze = " \
     file://0001-Add-initial-port-of-linux-gdbserver-add-gdb_proc_ser.patch \
     file://0002-Add-initial-port-of-linux-gdbserver-add-gdb_proc_ser.patch \
     file://0003-Initial-port-of-core-reading-support-Added-support-f.patch \
     file://0004-Fix-debug-message-when-register-is-unavailable.patch \
     file://0005-MicroBlaze-native-gdb-port.patch \
     file://0006-Adding-64-bit-MB-support-Added-new-architecture-to-M.patch \
     file://0007-these-changes-will-make-64-bit-vectors-as-default-ta.patch \
     file://0008-Added-m64-abi-for-64-bit-target-descriptions.-set-m6.patch \
     file://0009-Depth-Total-number-of-inline-functions-refer-inline-.patch \
     file://0010-Fix-gdb-14-build-errors-for-microblaze-xilinx-elf-20.patch \
     file://0011-fix-gdb-microblaze-xilinx-elf-crash-issue-on-invocat.patch \
     file://0013-Disable-the-warning-message-for-eh_frame_hdr.patch \
     file://0015-upstream-change-to-garbage-collection-sweep-causes-m.patch \
     file://0016-Add-new-bit-field-instructions.patch \
     file://0019-initial-support-for-MicroBlaze-64-bit-m64.patch \
     file://0020-initial-support-for-MicroBlaze-64-bit-m64.patch \
     file://0021-Added-relocations-for-MB-X.patch \
     file://0022-initial-support-for-MicroBlaze-64-bit-m64.patch \
     file://0023-Added-relocations-for-MB-X.patch \
     file://0025-Fixed-address-computation-issues-with-64bit-address-.patch \
     file://0028-fixing-the-long-long-long-mingw-toolchain-issue.patch \
     file://0029-Added-support-to-new-arithmetic-single-register-inst.patch \
     file://0030-double-imml-generation-for-64-bit-values.patch \
     file://0035-ld-emulparams-elf64microblaze-Fix-emulation-generati.patch \
     file://0036-Invalid-data-offsets-pointer-after-relaxation.-Propo.patch \
     file://0037-Double-free-with-ld-no-keep-memory.-Proposed-patches.patch \
     file://0038-MB-binutils-Upstream-port-issues.patch \
     file://0039-Initial-port-of-core-reading-support-Added-support-f.patch \
     file://0040-Fix-build-issues-after-Xilinx-2023.2-binutils-merge.patch \
     file://0041-disable-truncated-register-warning-gdb-remote.c.patch \
     file://0042-Fix-unresolved-conflicts-from-binutils_2_42_merge.patch \
     file://0043-microblaze_gdbarch_init-set-microblaze_abi-based-on-.patch \
     file://0044-Start-bfd_mach_microblaze-values-from-0-0-1-instead-.patch \
     file://0045-Fix-build-issues-bfd-reloc.c-add-missing-relocs-used.patch \
     file://0046-Regenerate-bfd-bfd-in2.h-bfd-libbfd.h.patch \
     file://0047-gdb-remote.c-revert-earlier-change-to-process_g_pack.patch \
     file://0048-Fix-build-issues-after-Xilinx-2023.2-binutils-patch-.patch \
     file://0049-Add-back-R_MICROBLAZE_NONE-for-linker-relaxation-pro.patch \
     file://0050-When-unwinding-pc-value-adjust-return-pc-value.patch \
     file://0051-info-reg-pc-does-not-print-symbolic-value.patch \
     file://0052-Wrong-target-description-accepted-by-microblaze-arch.patch \
     file://0053-Merge-gdb-microblaze-linux-tdep.c-to-gdb-14-and-fix-.patch \
     file://0054-Roll-back-an-improvement-which-inlines-target_gdbarc.patch \
     file://0055-fix-microblaze-linux-nat.patch \
     "
