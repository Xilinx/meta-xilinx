require recipes-bsp/u-boot/u-boot.inc
require u-boot-elf.inc

DEPENDS += "dtc-native"

# This revision corresponds to the tag "v2015.04"
SRCREV = "f33cdaa4c3da4a8fd35aa2f9a3172f31cc887b35"

PV = "v2015.04+git${SRCPV}"

