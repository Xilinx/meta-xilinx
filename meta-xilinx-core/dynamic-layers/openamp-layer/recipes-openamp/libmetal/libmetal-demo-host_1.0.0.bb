SUMMARY = "AMD Xilinx libmetal IRQ shared-memory demo for the host \
(Linux APU)."
DESCRIPTION = "Builds the libmetal IRQ / shared-memory demo \
(irq_shmem_demo) intended to run on the Linux host (APU) side, \
exercising the libmetal user-space abstractions."
require ${LAYER_PATH_openamp-layer}/recipes-openamp/rpmsg-examples/rpmsg-example.inc
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=ab88daf995c0bd0071c2e1e55f3d3505"
REPO = "git://github.com/Xilinx/openamp-system-reference.git;protocol=https"
SRCREV = "b747e54881c6ba3b0c1c6dd4e36db1985f73a0a8"
BRANCH = "2026"
PV .= "+git"

S = "${UNPACKDIR}/${BP}/examples/libmetal"
inherit pkgconfig cmake

DEPENDS:append = " libmetal "

# libmetal is a machine_arch recipe, so we are too
PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES:${PN} = " /usr/bin/irq_shmem_demo "

do_install () {
	install -d ${D}/usr/bin
	install -m 0755 ${B}/machine/host/amd_linux_userspace/irq_shmem_demo ${D}/usr/bin/irq_shmem_demo
}

EXTRA_OECMAKE:append = " \ 
        -DLIB_INSTALL_DIR=${libdir} \
        -DLIBEXEC_INSTALL_DIR=${libexecdir} \
        -DDEMO=irq_shmem_demo \
        -DROLE=host \
        -DPROJECT_MACHINE=amd_linux_userspace \
"

RDEPENDS:${PN}:append = " libudev kernel-module-uio-pdrv-genirq "
