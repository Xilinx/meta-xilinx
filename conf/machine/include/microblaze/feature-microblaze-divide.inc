TUNEVALID[divide-hard] = "Enable Microblaze hardware divider."
MBCCARGSDIV += "${@bb.utils.contains("TUNE_FEATURES", "divide-hard", "-mno-xl-soft-div", "-mxl-soft-div" ,d)}"
MBPKGDIV .= "${@bb.utils.contains("TUNE_FEATURES", "divide-hard", "-div", "" ,d)}"
