TUNEVALID[reorder] = "Enable Microblaze Reorder Instructions."
MBCCARGSREORDER = "${@bb.utils.contains("TUNE_FEATURES", "reorder", "-mxl-reorder", "" ,d)}"
MBPKGREORDER = "${@bb.utils.contains("TUNE_FEATURES", "reorder", "-re", "" ,d)}"
