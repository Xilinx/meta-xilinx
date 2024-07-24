DESCRIPTION = "Jupyter notebooks for openAMP"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=268f2517fdae6d70f4ea4c55c4090aa8"

inherit jupyter-examples

REPO ?= "git://github.com/Xilinx/OpenAMP-notebooks.git;protocol=https"
SRCREV ?= "30b76d864261e5dd321fadfaf74b933b7cd88892"
BRANCH ?= "main"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH') != '']}"
SRC_URI = "${REPO};${BRANCHARG}"
PV .= "+git"
S = "${WORKDIR}/git/openamp"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"
COMPATIBLE_MACHINE:versal = "versal"
COMPATIBLE_MACHINE:versal-net = "versal-net"

DEPENDS += " packagegroup-xilinx-jupyter \
             packagegroup-openamp"

RDEPENDS:${PN} = " packagegroup-xilinx-jupyter \
                   packagegroup-openamp"

do_install() {
    install -d ${D}/${JUPYTER_DIR}/openamp-notebooks
    install -d ${D}/${JUPYTER_DIR}/openamp-notebooks/pics

    install -m 0755 ${S}/*.ipynb    ${D}/${JUPYTER_DIR}/openamp-notebooks
    install -m 0755 ${S}/pics/*.png ${D}/${JUPYTER_DIR}/openamp-notebooks/pics
}
