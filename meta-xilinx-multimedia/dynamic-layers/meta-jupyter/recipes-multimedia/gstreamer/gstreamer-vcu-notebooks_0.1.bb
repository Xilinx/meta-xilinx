DESCRIPTION = "Jupyter notebook examples for VCU in ZynqMP-EV devices"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=17e31b2e971eed6471a361c7dc4faa18"

inherit jupyter-examples

require gstreamer-multimedia-notebooks_0.1.inc

S = "${WORKDIR}/git"

inherit features_check

REQUIRED_MACHINE_FEATURES = "vcu"
PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS:${PN} = "packagegroup-xilinx-jupyter packagegroup-xilinx-gstreamer gstreamer-vcu-examples start-jupyter"

EXTRA_OEMAKE = 'D=${D} JUPYTER_DIR=${JUPYTER_DIR}'

do_install() {
	oe_runmake -C ${S}/vcu install_vcu_notebooks
}
