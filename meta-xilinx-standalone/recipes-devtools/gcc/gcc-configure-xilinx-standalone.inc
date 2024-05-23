# Mimic the file gcc-configure-common from the Yocto Project
LINKER_HASH_STYLE:xilinx-standalone = ""
SYMVERS_CONF:xilinx-standalone = ""

EXTRA_OECONF:append:microblaze:xilinx-standalone = " --disable-initfini_array"

EXTRA_OECONF:append:microblaze:xilinx-standalone = " --disable-__cxa_atexit"
