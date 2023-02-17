require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot-tools.inc

require u-boot-xlnx-common.inc

PROVIDES:append = " u-boot-tools"
RPROVIDES:${PN} += "u-boot-tools"
RPROVIDES:${PN}-dbg += "u-boot-tools-dbg"
RPROVIDES:${PN}-dev += "u-boot-tools-dev"
RPROVIDES:${PN}-lic += "u-boot-tools-lic"
RPROVIDES:${PN}-src += "u-boot-tools-src"
RPROVIDES:${PN}-mkenvimage += "u-boot-tools-mkenvimage"
RPROVIDES:${PN}-mkimage += "u-boot-tools-mkimage"
