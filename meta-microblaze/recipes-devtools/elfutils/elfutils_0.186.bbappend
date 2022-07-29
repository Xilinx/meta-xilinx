# symver functions not currently supported on microblaze
EXTRA_OECONF:append:class-target:microblaze = " --disable-symbol-versioning"
