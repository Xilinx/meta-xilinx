DESCRIPTION = "Management Controller Driver Interface library"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.MIT;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/Xilinx-CNS/mcdi-lib.git;branch=${BRANCH};protocol=https"
BRANCH = "master"
SRCREV = "00e8422cbfc62c90b3a925b734b6c0caa2481540"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal-net = "${MACHINE}"

INSTALL_PATH = "${prefix}/share/${PN}/examples"

TARGET_CC_ARCH += "${LDFLAGS}"

do_compile() {
    oe_runmake all CC="${CC}" CROSS_COMPILE="${TARGET_PREFIX}"
}

do_install() {
    install -d ${D}/${libdir}/
    install -m 0644 ${B}/lib/libmcdi.so.${PV} ${D}/${libdir}/
    ln -sf ${PN}.so ${D}/${libdir}/${PN}.so.${PV}

    install -d ${D}/${INSTALL_PATH}
    install -m 0755 ${B}/example/mcdi_example_app ${D}/${INSTALL_PATH}
    install -m 0755 ${B}/init/init_app ${D}/${INSTALL_PATH}
}

FILES:${PN} = "${INSTALL_PATH}/* ${libdir}/*"
