# Automatically add the armasm option if the crypto extensions are available on armv8
PACKAGECONFIG_CRYPTO = ""
PACKAGECONFIG_CRYPTO:aarch64 = "${@bb.utils.contains("TUNE_FEATURES", "crypto", "armasm", "",d)}"

PACKAGECONFIG += "${PACKAGECONFIG_CRYPTO}"
PACKAGECONFIG[armasm] = "--enable-armasm,--disable-armasm"
