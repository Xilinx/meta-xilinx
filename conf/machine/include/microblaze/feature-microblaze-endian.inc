# This feature file must be included/required FIRST as it defines the start of the PKGARCH bitbake variable
MBCCARGSENDIAN = ""
MBPKGENDIAN = ""

TUNEVALID[little-endian] = "Use Microblaze Little Endian."
MBCCARGSENDIAN += "${@bb.utils.contains("TUNE_FEATURES", "little-endian", "-mlittle-endian", "" ,d)}"
MBPKGENDIAN .= "${@bb.utils.contains("TUNE_FEATURES", "little-endian", "el", "" ,d)}"

TUNEVALID[big-endian] = "Use Microblaze Big Endian."
MBCCARGSENDIAN += "${@bb.utils.contains("TUNE_FEATURES", "big-endian", "-mbig-endian", "" ,d)}"
MBPKGENDIAN .= "${@bb.utils.contains("TUNE_FEATURES", "big-endian", "eb", "" ,d)}"

TUNECONFLICTS[little-endian] = "big-endian"
