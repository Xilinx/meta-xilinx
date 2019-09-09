SUMMARY = "Xilinx PYNQ Library"
HOMEPAGE = "http://pynq.io"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b42e39ad2ddbad7e8ad47f3eee6feff5"

RDEPENDS_${PN} += "\
	${PYTHON_PN}-core \
	${PYTHON_PN}-asyncio \
	${PYTHON_PN}-cffi \
	${PYTHON_PN}-json \
	${PYTHON_PN}-math \
	${PYTHON_PN}-mmap \
	${PYTHON_PN}-multiprocessing \
	${PYTHON_PN}-numpy \
	${PYTHON_PN}-pycparser \
	${PYTHON_PN}-resource \
	${PYTHON_PN}-setuptools \
	${PYTHON_PN}-threading \
	${PYTHON_PN}-xml \
	lmsensors-libsensors \
	libdrm \
	libcma \
	pynq-overlay \
	"

inherit update-rc.d xilinx-pynq

DEPENDS += " libdrm boost libcma"

PYNQBRANCH ?= "image_v2.4"
SRCBRANCHARG = "${@['nobranch=1', 'branch=${PYNQBRANCH}'][d.getVar('PYNQBRANCH', True) != '']}"
PYNQURI ?= "git://github.com/Xilinx/PYNQ.git;protocol=https"

SRC_URI ?= "${PYNQURI};${SRCBRANCHARG} \
           file://pl_server_init \
	   "
SRCREV ?= "3d659d374701b7c34fa702e7aa23f71f9113f826"

FILESEXTRAPATHS_prepend := "${THISDIR}:"
S = "${WORKDIR}/git"

INSANE_SKIP_${PN} = "staticdev"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "pl_server_init"
INITSCRIPT_PARAMS = "start 99 S ."

do_install_append() {
	install -d ${D}${INIT_D_DIR}
	install -m 755 ${WORKDIR}/pl_server_init ${D}${INIT_D_DIR}/pl_server_init
}

FILES_${PN} += " ${INIT_D_DIR}/pl_server_init "
