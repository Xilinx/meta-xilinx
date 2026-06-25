DEFAULTTUNE ?= "microblaze"
SOC_FAMILY ?= "microblaze"

require conf/machine/include/soc-family.inc
require soc-tune-include.inc

# Linux Configuration
ARCH_DEFAULT_KERNELIMAGETYPE:microblaze = "linux.bin.ub"
