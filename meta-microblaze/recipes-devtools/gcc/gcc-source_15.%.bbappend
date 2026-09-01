# Add MicroBlaze Patches (only when using MicroBlaze)
FILESEXTRAPATHS:append := ":${THISDIR}/gcc-15"

# MicroBlaze patches use mixed tab/space indentation; ignore whitespace differences
QUILT_PATCHES_FUZZ = "2"
export QUILT_PATCH_OPTS = "--ignore-whitespace"


SRC_URI += " \
    file://mb32/0001-LOCAL-Testsuite-builtins-tests-require-fpic-Signed-o.patch \
    file://mb32/0002-Quick-fail-g-.dg-opt-memcpy1.C-This-particular-testc.patch \
    file://mb32/0003-For-dejagnu-static-testing-on-qemu-suppress-warnings.patch \
    file://mb32/0004-Add-MicroBlaze-to-target-supports-for-atomic-buil.-..patch \
    file://mb32/0005-Update-MicroBlaze-strings-test-for-new-scan-assembly.patch \
    file://mb32/0006-Allow-MicroBlaze-.weakext-pattern-in-regex-match-Ext.patch \
    file://mb32/0007-Add-MicroBlaze-to-check_profiling_available-Testsuit.patch \
    file://mb32/0008-Fix-the-Microblaze-crash-with-msmall-divides-flag-Co.patch \
    file://mb32/0009-Added-ashrsi3_with_size_opt-Added-ashrsi3_with_size_.patch \
    file://mb32/0010-Use-bralid-for-profiler-calls-Signed-off-by-Edgar-E..patch \
    file://mb32/0011-Removed-moddi3-routinue-Using-the-default-moddi3-fun.patch \
    file://mb32/0012-Add-INIT_PRIORITY-support-Added-TARGET_ASM_CONSTRUCT.patch \
    file://mb32/0013-Add-optimized-lshrsi3-When-barrel-shifter-is-not-pre.patch \
    file://mb32/0014-Add-cbranchsi4_reg-This-patch-optimizes-the-generati.patch \
    file://mb32/0015-Inline-Expansion-of-fsqrt-builtin.-The-changes-are-m.patch \
    file://mb32/0016-microblaze.md-Improve-adddi3-and-subdi3-insn-definit.patch \
    file://mb32/0017-Update-ashlsi3-movsf-patterns-This-patch-removes-the.patch \
    file://mb32/0018-8-stage-pipeline-for-microblaze-This-patch-adds-the-.patch \
    file://mb32/0019-Correct-the-const-high-double-immediate-value-with-t.patch \
    file://mb32/0020-Fix-internal-compiler-error-with-msmall-divides-This.patch \
    file://mb32/0021-Fix-the-calculation-of-high-word-in-a-long-long-64-b.patch \
    file://mb32/0022-this-patch-has-1.Fixed-the-bug-in-version-calculatio.patch \
    file://mb32/0023-Fixing-the-issue-with-the-builtin_alloc.-register-r1.patch \
    file://mb32/0024-Removed-fsqrt-generation-for-double-values.patch \
    file://mb64/0025-Intial-commit-of-64-bit-Microblaze.patch \
    file://mb64/0026-Intial-commit-for-64bit-MB-sources.-Need-to-cleanup-.patch \
    file://mb32/0027-re-arrangement-of-the-compare-branches.patch \
    file://mb32/0028-previous-commit-broke-the-handling-of-SI-Branch-comp.patch \
    file://mb64/0029-Support-of-multilibs-with-m64.patch \
    file://mb32/0030-Fixed-issues-like-1-Interrupt-alignment-issue-2-Sign.patch \
    file://mb64/0031-fixed-below-issues-Floating-point-print-issues-in-64.patch \
    file://mb64/0032-Added-double-arith-instructions-Fixed-prologue-stack.patch \
    file://mb64/0033-Fixed-the-load-store-issue-with-the-32bit-arith-libr.patch \
    file://mb64/0034-extending-the-Dwarf-support-to-64bit-Microblaze.patch \
    file://mb64/0035-fixing-the-typo-errors-in-umodsi3-file.patch \
    file://mb32/0036-fixing-the-32bit-LTO-related-issue9-1014024.patch \
    file://mb32/0037-Fixed-the-missing-stack-adjustment-in-prologue-of-mo.patch \
    file://mb64/0038-corrected-SPN-for-dlong-instruction-mapping.patch \
    file://mb64/0039-fixing-the-long-long-long-mingw-toolchain-issue.patch \
    file://mb64/0040-Fix-the-MB-64-bug-of-handling-QI-objects.patch \
    file://mb32/0041-We-will-check-the-possibility-of-peephole2-optimizat.patch \
    file://mb64/0042-fixed-typos-in-mul-div-and-mod-assembly-files.patch \
    file://mb64/0043-MB-64-removal-of-barrel-shift-instructions-from-defa.patch \
    file://mb64/0044-Added-new-MB-64-single-register-arithmetic-instructi.patch \
    file://mb64/0045-Added-support-for-64-bit-Immediate-values.patch \
    file://mb32/0046-Fix-Compiler-crash-with-freg-struct-return-This-patc.patch \
    file://mb32/0047-Add-TARGET_OPTION_OPTIMIZATION-and-disable-fivopts-b.patch \
    file://mb32/0048-Reducing-Stack-space-for-arguments.patch \
    file://mb32/0049-If-we-use-break_handler-attribute-then-interrupt-vec.patch \
    file://mb64/0050-Add-Zero_extended-instructions.patch \
    file://mb32/0051-Fix-failure-with-gcc.c-torture-execute-ashrdi-1.c-Os.patch \
    file://mb32/0052-Restrict-the-multilib-set-to-32-bit-variants.patch \
    file://mb64/0003-microblaze-do-not-offer-__int128-for-MB-64.patch \
    file://mb64/0004-libstdc-build-fast_float-without-a-128-bit-integer.patch \
    file://microblaze-mulitlib-hack.patch \
"
