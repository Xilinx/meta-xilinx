SUMMARY = "Install user script to support fpga-manager"
DESCRIPTION = "Install user script that loads and unloads overlays using kernel fpga-manager"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/fpgautil.c;beginline=1;endline=7;md5=1948a0c515a0c9ff5c6b29df82e81efd"

SRC_URI = "\
	file://fpgautil.c \
	"
S = "${WORKDIR}"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

do_compile() {
	${CC} ${LDFLAGS} fpgautil.c -o fpgautil
}

do_install() {
        install -Dm 0755 ${S}/fpgautil ${D}${bindir}/fpgautil
}

FILES:${PN} = "\
        ${bindir}/fpgautil \
        "
