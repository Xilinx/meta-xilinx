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

TUNECONFLICTS[v8.00] += "reorder"
TUNECONFLICTS[v8.10] += "reorder"
TUNECONFLICTS[v8.20] += "reorder"

# Perform some additional sanity checking
python __anonymous () {
    import bb
    tune_features = bb.data.getVar('TUNE_FEATURES', d, 1)
    tuneslist = tune_features.split()

    #
    # GCC will fail on v8.30 if reorder and pattern-compare are not 
    # both in ccflags
    # -mxl-reorder requires -mxl-pattern-compare for -mcpu=v8.30.a
    # Check if either one exists alone and if so, add the other
    #
    if 'v8.30' in tuneslist:
        if 'reorder' in tuneslist and 'pattern-compare' not in tuneslist:
            d.setVar("TUNE_FEATURES", "%s pattern-compare" % tune_features)
}
