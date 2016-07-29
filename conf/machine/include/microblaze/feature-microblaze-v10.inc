# Setup version 'feature set' for supported Microblaze version 10 versions

TUNEVALID[v10.0] = "Use Microblaze version 10.0"
MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v10.0", "-mcpu=v10.0", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v10.0", "-v10.0", "" ,d)}"
