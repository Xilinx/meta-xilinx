SUMMARY = "AMD Xilinx libmetal hardware abstraction library (xlnx \
fork)."
DESCRIPTION = "AMD Xilinx fork of the OpenAMP libmetal library, \
providing the user-space and baremetal hardware abstraction layer \
(memory, IRQ, I/O) used by open-amp on Zynq UltraScale+ MPSoC, Versal \
and Versal NET inter-processor communication."
SRCBRANCH ?= "2026"
SRCREV = "ba381ae6281b70d91b42a39ce6b9d5fb46259098"
BRANCH = "2026"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=f4d5df0f12dcea1b1a0124219c0dbab4"
PV .= "+git"

REPO = "git://github.com/Xilinx/libmetal.git;protocol=https"

include ${LAYER_PATH_openamp-layer}/recipes-openamp/libmetal/libmetal.inc
PACKAGE_ARCH = "${TUNE_PKGARCH}"
EXTRA_OECMAKE:append = " -DPROJECT_VENDOR=xlnx "

RPROVIDES:${PN}-dbg += "libmetal-dbg"
RPROVIDES:${PN}-dev += "libmetal-dev"
RPROVIDES:${PN}-lic += "libmetal-lic"
RPROVIDES:${PN}-src += "libmetal-src"
RPROVIDES:${PN}-staticdev += "libmetal-staticdev"
