# Verify meta-microblaze is part of the config!
INHERIT += "xilinx-microblaze"

# It is expected that the machine will define
# DEFAULTTUNE = "microblaze-v"
# TUNE_FEATURES:tune-microblaze-v = "${@bv.tune.riscv_isa_to_tune("<ISA>")}"

require conf/machine/include/mbv/arch-riscv.inc

AVAILTUNES += "microblaze-v"
PACKAGE_EXTRA_ARCHS:tune-microblaze-v = "${TUNE_RISCV_PKGARCH}"
