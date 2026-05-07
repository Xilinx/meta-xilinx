SUMMARY = "AMD Xilinx libmetal IRQ shared-memory demo for the host \
(Linux APU)."
DESCRIPTION = "Builds the libmetal IRQ / shared-memory demo \
(irq_shmem_demo) intended to run on the Linux host (APU) side, \
exercising the libmetal user-space abstractions."
require ${LAYER_PATH_openamp-layer}/recipes-openamp/rpmsg-examples/rpmsg-example.inc
LIC_FILES_CHKSUM ?= "file://LICENSE.md;md5=ab88daf995c0bd0071c2e1e55f3d3505"
REPO = "git://github.com/Xilinx/openamp-system-reference.git;protocol=https"
SRCREV = "b8a532d310b942005123dcb9f3c977a5819f03be"
BRANCH = "2026"
PV .= "+git"

S = "${WORKDIR}/git/examples/libmetal"
inherit pkgconfig cmake

DEPENDS:append = " libmetal "

FILES:${PN} = " /usr/bin/irq_shmem_demo /usr/bin/discover_platform.sh "

do_install () {
	install -d ${D}/usr/bin
	install -m 0755 ${B}/machine/host/amd_linux_userspace/irq_shmem_demo ${D}/usr/bin/irq_shmem_demo
	install -m 0755 ${S}/machine/host/amd_linux_userspace/discover_platform.sh ${D}/usr/bin/discover_platform.sh
}

EXTRA_OECMAKE:append = " \ 
        -DLIB_INSTALL_DIR=${libdir} \
        -DLIBEXEC_INSTALL_DIR=${libexecdir} \
	"

cmake_do_generate_toolchain_file:append() {
  cat >> ${WORKDIR}/toolchain.cmake <<EOF
    set (DEMO irq_shmem_demo)
    set (ROLE host)
    set (PROJECT_MACHINE amd_linux_userspace)
EOF
}

RDEPENDS:${PN}:append = " libudev kernel-module-uio-pdrv-genirq "
