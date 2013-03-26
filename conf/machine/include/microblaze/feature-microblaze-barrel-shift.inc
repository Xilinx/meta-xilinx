TUNEVALID[barrel-shift] = "Enable Microblaze Hardware Barrel Shift."
MBCCARGSBARRELSHIFT += "${@bb.utils.contains("TUNE_FEATURES", "barrel-shift", "-mxl-barrel-shift", "" ,d)}"
MBPKGBARRELSHIFT .= "${@bb.utils.contains("TUNE_FEATURES", "barrel-shift", "-bs", "" ,d)}"
