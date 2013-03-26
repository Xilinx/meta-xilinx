# Setup version 'feature set' for supported Microblaze version 8 versions
TUNEVALID[v850] = "Use Microblaze version 8.50"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v850", "-mcpu=v8.50.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v850", "-v850", "" ,d)}"

TUNEVALID[v840] = "Use Microblaze version 8.40"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v840", "-mcpu=v8.40.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v840", "-v840", "" ,d)}"

TUNEVALID[v830] = "Use Microblaze version 8.30"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v830", "-mcpu=v8.30.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v830", "-v830", "" ,d)}"

TUNEVALID[v820] = "Use Microblaze version 8.20"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v820", "-mcpu=v8.20.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v820", "-v820", "" ,d)}"

TUNEVALID[v810] = "Use Microblaze version 8.10"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v810", "-mcpu=v8.10.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v810", "-v810", "" ,d)}"

TUNEVALID[v800] = "Use Microblaze version 8.00"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v800", "-mcpu=v8.00.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v800", "-v800", "" ,d)}"
