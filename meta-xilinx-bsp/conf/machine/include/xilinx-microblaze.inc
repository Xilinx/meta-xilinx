# To avoid the require overriding previous defaults we use the override to make this the default
TUNE_FEATURES_save := "${TUNE_FEATURES:tune-microblaze}"

require conf/machine/include/microblaze/tune-microblaze.inc

# Reset the default value back to what it was previously set to...
TUNE_FEATURES:tune-microblaze := "${TUNE_FEATURES_save}"
