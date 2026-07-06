# Add MicroBlaze Patches (only when using MicroBlaze)
FILESEXTRAPATHS:append := ":${THISDIR}/gcc-15"

# MicroBlaze patches use mixed tab/space indentation; ignore whitespace differences
QUILT_PATCHES_FUZZ = "2"
export QUILT_PATCH_OPTS = "--ignore-whitespace"

SRC_URI += " \
    file://0001-LOCAL-Testsuite-builtins-tests-require-fpic-Signed-o.patch \
    file://0002-Quick-fail-g-.dg-opt-memcpy1.C-This-particular-testc.patch \
    file://0004-Add-MicroBlaze-to-target-supports-for-atomic-buil.-..patch \
    file://0005-Update-MicroBlaze-strings-test-for-new-scan-assembly.patch \
    file://0010-Fix-the-Microblaze-crash-with-msmall-divides-flag-Co.patch \
    file://0011-Added-ashrsi3_with_size_opt-Added-ashrsi3_with_size_.patch \
    file://0012-Use-bralid-for-profiler-calls-Signed-off-by-Edgar-E..patch \
    file://0014-Add-INIT_PRIORITY-support-Added-TARGET_ASM_CONSTRUCT.patch \
    file://0015-Add-optimized-lshrsi3-When-barrel-shifter-is-not-pre.patch \
    file://0016-Add-cbranchsi4_reg-This-patch-optimizes-the-generati.patch \
    file://0017-Inline-Expansion-of-fsqrt-builtin.-The-changes-are-m.patch \
    file://0018-microblaze.md-Improve-adddi3-and-subdi3-insn-definit.patch \
    file://0019-Update-ashlsi3-movsf-patterns-This-patch-removes-the.patch \
    file://0020-8-stage-pipeline-for-microblaze-This-patch-adds-the-.patch \
    file://0021-Correct-the-const-high-double-immediate-value-with-t.patch \
    file://0022-Fix-internal-compiler-error-with-msmall-divides-This.patch \
    file://0023-Fix-the-calculation-of-high-word-in-a-long-long-64-b.patch \
    file://0024-this-patch-has-1.Fixed-the-bug-in-version-calculatio.patch \
    file://0025-Fixing-the-issue-with-the-builtin_alloc.-register-r1.patch \
    file://0026-Removed-fsqrt-generation-for-double-values.patch \
    file://0027-Intial-commit-of-64-bit-Microblaze.patch \
    file://0028-Intial-commit-for-64bit-MB-sources.-Need-to-cleanup-.patch \
    file://0029-re-arrangement-of-the-compare-branches.patch \
    file://0030-previous-commit-broke-the-handling-of-SI-Branch-comp.patch \
    file://0031-Support-of-multilibs-with-m64.patch \
    file://0032-Fixed-issues-like-1-Interrupt-alignment-issue-2-Sign.patch \
    file://0033-fixed-below-issues-Floating-point-print-issues-in-64.patch \
    file://0034-Added-double-arith-instructions-Fixed-prologue-stack.patch \
    file://0050-Add-TARGET_OPTION_OPTIMIZATION-and-disable-fivopts-b.patch \
    file://0051-Reducing-Stack-space-for-arguments.patch \
    file://0053-Add-Zero_extended-instructions.patch \
    file://0054-Fix-failure-with-gcc.c-torture-execute-ashrdi-1.c-Os.patch \
    file://0055-Add_define_expand_muldi3_to_optimize_muldi3_using_32.patch \
    file://0056-Fix_muldi3_-_enable_only_when_TARGET_MULTIPLY_HIGH_is.patch \
    file://microblaze-mulitlib-hack.patch \
"
