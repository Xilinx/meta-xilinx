require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot-tools.inc

require u-boot-xlnx-common.inc

PROVIDES:append = " u-boot-tools"
RPROVIDES:${PN}:append = " u-boot-tools"
