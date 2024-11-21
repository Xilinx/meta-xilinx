# Add MicroBlaze Patches (only when using MicroBlaze)
FILESEXTRAPATHS:append := ":${THISDIR}/gcc-13"

# Our changes are all local, no real patch-status
ERROR_QA:remove = "patch-status"

SRC_URI += " \
    file://0001-LOCAL-Testsuite-builtins-tests-require-fpic-Signed-o.patch \
    file://0002-Quick-fail-g-.dg-opt-memcpy1.C-This-particular-testc.patch \
    file://0003-For-dejagnu-static-testing-on-qemu-suppress-warnings.patch \
    file://0004-Add-MicroBlaze-to-target-supports-for-atomic-buil.-..patch \
    file://0005-Update-MicroBlaze-strings-test-for-new-scan-assembly.patch \
    file://0006-Allow-MicroBlaze-.weakext-pattern-in-regex-match-Ext.patch \
    file://0007-Add-MicroBlaze-to-check_profiling_available-Testsuit.patch \
    file://0008-Fix-atomic-side-effects.-In-atomic_compare_and_swaps.patch \
    file://0009-Fix-atomic-boolean-return-value.-In-atomic_compare_a.patch \
    file://0010-Fix-the-Microblaze-crash-with-msmall-divides-flag-Co.patch \
    file://0011-Added-ashrsi3_with_size_opt-Added-ashrsi3_with_size_.patch \
    file://0012-Use-bralid-for-profiler-calls-Signed-off-by-Edgar-E..patch \
    file://0013-Removed-moddi3-routinue-Using-the-default-moddi3-fun.patch \
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
    file://0035-Fixed-the-issue-in-the-delay-slot-with-swap-instruct.patch \
    file://0036-Fixed-the-load-store-issue-with-the-32bit-arith-libr.patch \
    file://0037-extending-the-Dwarf-support-to-64bit-Microblaze.patch \
    file://0038-fixing-the-typo-errors-in-umodsi3-file.patch \
    file://0039-fixing-the-32bit-LTO-related-issue9-1014024.patch \
    file://0040-Fixed-the-missing-stack-adjustment-in-prologue-of-mo.patch \
    file://0041-corrected-SPN-for-dlong-instruction-mapping.patch \
    file://0042-fixing-the-long-long-long-mingw-toolchain-issue.patch \
    file://0043-Fix-the-MB-64-bug-of-handling-QI-objects.patch \
    file://0044-We-will-check-the-possibility-of-peephole2-optimizat.patch \
    file://0045-fixed-typos-in-mul-div-and-mod-assembly-files.patch \
    file://0046-MB-64-removal-of-barrel-shift-instructions-from-defa.patch \
    file://0047-Added-new-MB-64-single-register-arithmetic-instructi.patch \
    file://0048-Added-support-for-64-bit-Immediate-values.patch \
    file://0049-Fix-Compiler-crash-with-freg-struct-return-This-patc.patch \
    file://0050-Add-TARGET_OPTION_OPTIMIZATION-and-disable-fivopts-b.patch \
    file://0051-Reducing-Stack-space-for-arguments.patch \
    file://0052-If-we-use-break_handler-attribute-then-interrupt-vec.patch \
    file://0053-Add-Zero_extended-instructions.patch \
    file://0054-Fix-failure-with-gcc.c-torture-execute-ashrdi-1.c-Os.patch \
    file://microblaze-mulitlib-hack.patch \
"
