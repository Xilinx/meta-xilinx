SUMMARY = "Udev rules files for Linux drivers"
DESCRIPTION = "Generic udev rules recipe for Xilinx Linux in tree drivers"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://99-aie-device.rules \
"

S = "${WORKDIR}"

inherit useradd

COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:zynq = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:microblaze = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"

do_configure[noexec] = '1'
do_compile[noexec] = '1'

do_install () {
    install -d ${D}${sysconfdir}/udev/rules.d
    for rule in $(find ${WORKDIR} -maxdepth 1 -type f -name "*.rules"); do
        install -m 0644 $rule ${D}${sysconfdir}/udev/rules.d/
    done
}

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} += "-r aie;"

FILES:${PN} += "${sysconfdir}/udev/rules.d/*"

