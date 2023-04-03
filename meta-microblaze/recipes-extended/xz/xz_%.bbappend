# Microblaze doesn't support versioned symbols
#
#  ../../../xz-5.4.1/src/liblzma/common/stream_encoder_mt.c:1283:1: error: symver is only supported on ELF platforms
#
EXTRA_OECONF:append:microblaze = " --disable-symbol-versions"
