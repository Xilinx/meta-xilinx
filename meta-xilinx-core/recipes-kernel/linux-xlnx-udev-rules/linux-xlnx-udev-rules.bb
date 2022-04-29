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

do_configure[noexec] = '1'
do_compile[noexec] = '1'

do_install () {
    install -d ${D}${sysconfdir}/udev/rules.d
    for rule in $(find ${WORKDIR} -maxdepth 1 -type f -name "*.rules"); do
        if ${@bb.utils.contains_any('SOC_VARIANT', ['ai-core', 'ai-edge'], 'true', 'false' ,d)}; then
            install -m 0644 ${WORKDIR}/99-aie-device.rules ${D}${sysconfdir}/udev/rules.d/
        fi
        install -m 0644 $rule ${D}${sysconfdir}/udev/rules.d/
    done
}

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} += "${@bb.utils.contains_any('SOC_VARIANT', ['ai-core', 'ai-edge'], '-r aie;', '', d)}"

FILES:${PN} += "${sysconfdir}/udev/rules.d/*"

