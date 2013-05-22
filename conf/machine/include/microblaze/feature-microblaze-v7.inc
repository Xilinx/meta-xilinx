# Setup version 'feature set' for supported Microblaze version 7 versions
TUNEVALID[v7.30] = "Use Microblaze version 7.30"

MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v7.30", "-mcpu=v7.30.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v7.30", "-v7.30", "" ,d)}"

TUNECONFLICTS[v7.30] += "reorder little-endian"
