DESCRIPTION = "Jupyter notebook examples for Platform Management (PM) in Versal devices"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://../../LICENSE;md5=268f2517fdae6d70f4ea4c55c4090aa8"

inherit jupyter-examples

SRC_URI = "git://github.com/Xilinx/platform-management-notebooks.git;branch=xlnx_rel_v2023.2;protocol=https \
           file://LICENSE \
           "

SRCREV = "c502be361b6857e21ab903f31c9ead69e3a0d9ba"

S = "${WORKDIR}/git/pm-notebooks"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal = "versal"
COMPATIBLE_MACHINE:versal-net = "versal-net"

RDEPENDS:${PN} = " \
    packagegroup-xilinx-jupyter \
    python3-ipywidgets \
    python3-pydot \
    graphviz \
    "

do_install() {
    install -d ${D}/${JUPYTER_DIR}/pm-notebooks
    install -d ${D}/${JUPYTER_DIR}/pm-notebooks/pmutil
    install -d ${D}/${JUPYTER_DIR}/pm-notebooks/pmutil/data

    install -m 0644 ${S}/README ${D}/${JUPYTER_DIR}/pm-notebooks
    install -m 0755 ${S}/*.ipynb ${D}/${JUPYTER_DIR}/pm-notebooks
    install -m 0755 ${S}/pmutil/*.py ${D}/${JUPYTER_DIR}/pm-notebooks/pmutil
    install -m 0755 ${S}/pmutil/data/*.png ${D}/${JUPYTER_DIR}/pm-notebooks/pmutil/data
}

