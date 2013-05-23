TUNEVALID[reorder] = "Enable Microblaze Reorder Instructions."
MBCCARGSREORDER = "${@bb.utils.contains("TUNE_FEATURES", "reorder", "-mxl-reorder", "-mno-xl-reorder" ,d)}"
MBPKGREORDER = "${@bb.utils.contains("TUNE_FEATURES", "reorder", "-re", "" ,d)}"
