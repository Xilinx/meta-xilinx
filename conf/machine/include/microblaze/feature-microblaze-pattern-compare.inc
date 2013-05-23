TUNEVALID[pattern-compare] = "Enable Microblaze Pattern Compare Instructions."
MBCCARGSPATTERNCOMPARE = "${@bb.utils.contains("TUNE_FEATURES", "pattern-compare", "-mxl-pattern-compare", "-mno-xl-pattern-compare" ,d)}"
MBPKGSPATTERNCOMPARE = "${@bb.utils.contains("TUNE_FEATURES", "pattern-compare", "-cmp", "" ,d)}"
