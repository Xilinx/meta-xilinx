# Verify meta-microblaze is part of the config!
INHERIT += "xilinx-microblaze"

# To avoid the require overriding previous defaults we use the override to make this the default
TUNE_FEATURES_save := "${TUNE_FEATURES:tune-microblaze}"

require conf/machine/include/microblaze/tune-microblaze.inc

# Reset the default value back to what it was previously set to...
TUNE_FEATURES:tune-microblaze := "${TUNE_FEATURES_save}"

# Various microblaze architecture defaults

# microblaze does not get on with pie for reasons not looked into as yet
GCCPIE:microblaze = ""
GLIBCPIE:microblaze = ""
SECURITY_CFLAGS:microblaze = ""
SECURITY_LDFLAGS:microblaze = ""
# Microblaze does not support gnu hash style
LINKER_HASH_STYLE:microblaze = "sysv"
