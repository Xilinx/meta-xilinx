SUMMARY = "Install user script to support fpga-manager"
DESCRIPTION = "Install user script that loads and unloads overlays using kernel fpga-manager"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${WORKDIR}/fpgautil.c;beginline=1;endline=24;md5=8010e59a286b1e3a73a9fdd93bd18778"

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
