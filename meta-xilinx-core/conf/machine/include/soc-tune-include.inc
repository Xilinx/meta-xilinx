DEFAULTTUNE ??= "armv8a"

# Unfortunately various tunefiles don't include each other, so create
# a list of things to require based on the DEFAULTTUNE setting.
TUNEFILE[cortexr5] = "conf/machine/include/arm/armv7r/tune-cortexr5.inc"
TUNEFILE[cortexa9thf-neon] = "conf/machine/include/arm/armv7a/tune-cortexa9.inc"
TUNEFILE[armv8a] = "conf/machine/include/arm/arch-armv8a.inc"
TUNEFILE[cortexa53] = "conf/machine/include/arm/armv8a/tune-cortexa53.inc"
TUNEFILE[cortexa72] = "conf/machine/include/arm/armv8a/tune-cortexa72.inc"
TUNEFILE[cortexa72-cortexa53] = "conf/machine/include/arm/armv8a/tune-cortexa72-cortexa53.inc"
TUNEFILE[microblaze] = "conf/machine/include/xilinx-microblaze.inc"

# Default to arch-armv8a.inc
TUNEFILE = "${@ d.getVarFlag('TUNEFILE', d.getVar('DEFAULTTUNE')) or 'conf/machine/include/arm/arch-armv8a.inc'}"

require ${TUNEFILE}
