# Setup version 'feature set' for supported Microblaze version 9 versions
TUNEVALID[v9.6] = "Use Microblaze version 9.6"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v9.6", "-mcpu=v9.6", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v9.6", "-v9.6", "" ,d)}"

TUNEVALID[v9.0] = "Use Microblaze version 9.0"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v9.0", "-mcpu=v9.0", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v9.0", "-v9.0", "" ,d)}"
