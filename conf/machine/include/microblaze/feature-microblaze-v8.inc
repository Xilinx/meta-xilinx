# Setup version 'feature set' for supported Microblaze version 8 versions
TUNEVALID[v8.50] = "Use Microblaze version 8.50"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v8.50", "-mcpu=v8.50.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v8.50", "-v8.50", "" ,d)}"

TUNEVALID[v8.40] = "Use Microblaze version 8.40"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v8.40", "-mcpu=v8.40.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v8.40", "-v8.40", "" ,d)}"

TUNEVALID[v8.30] = "Use Microblaze version 8.30"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v8.30", "-mcpu=v8.30.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v8.30", "-v8.30", "" ,d)}"

TUNEVALID[v8.20] = "Use Microblaze version 8.20"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v8.20", "-mcpu=v8.20.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v8.20", "-v8.20", "" ,d)}"

TUNEVALID[v8.10] = "Use Microblaze version 8.10"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v8.10", "-mcpu=v8.10.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v8.10", "-v8.10", "" ,d)}"

TUNEVALID[v8.00] = "Use Microblaze version 8.00"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v8.00", "-mcpu=v8.00.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v8.00", "-v8.00", "" ,d)}"
