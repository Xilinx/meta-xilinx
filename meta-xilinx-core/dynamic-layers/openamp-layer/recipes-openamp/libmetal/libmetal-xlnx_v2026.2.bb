SUMMARY = "AMD Xilinx libmetal hardware abstraction library (xlnx \
fork)."
DESCRIPTION = "AMD Xilinx fork of the OpenAMP libmetal library, \
providing the user-space and baremetal hardware abstraction layer \
(memory, IRQ, I/O) used by open-amp on Zynq UltraScale+ MPSoC, Versal \
and Versal NET inter-processor communication."
SRCBRANCH ?= "main"
SRCREV = "c5389ea380a2824a069f06df357d7dddfc0942a9"
BRANCH = "main"
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=f4d5df0f12dcea1b1a0124219c0dbab4"
PV .= "+git"

include ${LAYER_PATH_openamp-layer}/recipes-openamp/libmetal/libmetal.inc
PACKAGE_ARCH = "${TUNE_PKGARCH}"

# udev/eudev are Linux-only; strip them from DEPENDS for baremetal targets
DEPENDS:remove = "${@'' if 'linux' in (d.getVar('TARGET_OS') or '') else bb.utils.contains('DISTRO_FEATURES', 'systemd', 'udev', 'eudev', d)}"
EXTRA_OECMAKE:append = " -DPROJECT_VENDOR=xlnx "

RPROVIDES:${PN}-dbg += "libmetal-dbg"
RPROVIDES:${PN}-dev += "libmetal-dev"
RPROVIDES:${PN}-lic += "libmetal-lic"
RPROVIDES:${PN}-src += "libmetal-src"
RPROVIDES:${PN}-staticdev += "libmetal-staticdev"
