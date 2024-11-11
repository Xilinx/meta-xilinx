S = "${WORKDIR}"

FILES:${PN} += "${JUPYTER_DIR}"

RDEPENDS:${PN} ?= "packagegroup-xilinx-jupyter"

JUPYTER_DIR ?= "${datadir}/example-notebooks"

do_install() {
    install -d ${D}/${JUPYTER_DIR}/${PN}
    install -m 0755 ${S}/*.ipynb ${D}/${JUPYTER_DIR}/${PN}/
}

EXCLUDE_FROM_WORLD = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

