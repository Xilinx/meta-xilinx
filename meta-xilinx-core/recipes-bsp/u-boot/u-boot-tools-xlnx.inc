SUMMARY = "U-Boot host tools (mkimage, mkenvimage, ...) built from the \
AMD Xilinx u-boot-xlnx tree."
DESCRIPTION = "Provides the U-Boot host-side utilities (mkimage, \
mkenvimage, dumpimage, fit_check_sign, ...) built from the AMD Xilinx \
u-boot-xlnx source so that boot-image and FIT generation use the same \
image format support as the on-target u-boot-xlnx."
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
