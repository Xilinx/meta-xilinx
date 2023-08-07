DESCRIPTION = "Management Controller Driver Interface library"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.MIT;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI = "git://github.com/Xilinx-CNS/mcdi-lib.git;branch=${BRANCH};protocol=https"
BRANCH = "master"
SRCREV = "db448189e5fcb38b4750faf6afe243d7998863bc"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal-net = "${MACHINE}"

MCDI_PATH_EXAMPLES = "${datadir}/${BPN}/examples"

do_compile() {
    oe_runmake all CC="${CC}" CROSS_COMPILE="${TARGET_PREFIX}"
}

do_install() {
    oe_runmake install prefix="${prefix}" DESTDIR="${D}"

    install -d ${D}/${MCDI_PATH_EXAMPLES}
    install -m 0755 ${B}/example/mcdi_example_app ${D}/${MCDI_PATH_EXAMPLES}
    install -m 0755 ${B}/init/init_app ${D}/${MCDI_PATH_EXAMPLES}
}

PACKAGES =+ "${PN}-examples"

FILES:${PN}-examples = " \
    ${MCDI_PATH_EXAMPLES}/* \
    "
