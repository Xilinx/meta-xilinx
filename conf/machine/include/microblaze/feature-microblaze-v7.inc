# Setup version 'feature set' for supported Microblaze version 7 versions
TUNEVALID[v730] = "Use Microblaze version 7.30"

MBCCARGSVERSION += "${@bb.utils.contains("TUNE_FEATURES", "v730", "-mcpu=v7.30.a", "" ,d)}"
MBPKGVERSION .= "${@bb.utils.contains("TUNE_FEATURES", "v730", "-v730", "" ,d)}"

TUNECONFLICTS[v730] += "reorder little-endian"
