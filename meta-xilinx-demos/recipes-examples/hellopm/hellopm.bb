#
# This recipe installs start script hellopm.sh in /usr/bin directory
#

DESCRIPTION = "This is a Power management Linux application which will allow user to do various operations such as suspend,wakeup,reboot,shutdown the system."
SUMMARY = "Hello PM Management Linux Application"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${WORKDIR}/hellopm.sh;beginline=3;endline=28;md5=bae95b94bf30629240b67f175cfd05ed"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:zynqmp = "zynqmp"

SRC_URI = "\
	file://hellopm.sh \
	"

S = "${WORKDIR}"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

do_install() {
	install -Dm 0755 ${S}/hellopm.sh ${D}${bindir}/hellopm
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
