SUMMARY = "Xen u-boot script generation using image builder"
DESCRIPTION = "Generates a U-Boot boot script that hands control to \
the Xen hypervisor and launches the configured DomU images, built from \
the AMD Xilinx Xen image-builder output."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy

DEPENDS += " \
	image-builder-native \
	bash \
	"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-2ve-2vm = ".*"

# Default XEN_TEMPLATE_CONFIG is set to <soc>-xen-scr-dom0-template-cfg file
# User can change to <soc>-xen-scr-dom0-template-cfg or a custom config.

XEN_TEMPLATE_CONFIG_DEFAULT = ""
XEN_TEMPLATE_CONFIG_DEFAULT:zynqmp = "zynqmp-xen-scr-dom0-template-cfg"
XEN_TEMPLATE_CONFIG_DEFAULT:versal = "versal-xen-scr-dom0-template-cfg"
XEN_TEMPLATE_CONFIG_DEFAULT:versal-2ve-2vm = "versal-2ve-2vm-xen-scr-dom0-template-cfg"
XEN_TEMPLATE_CONFIG ?= "${XEN_TEMPLATE_CONFIG_DEFAULT}"

SRC_URI = " \
    ${@'file://${XEN_TEMPLATE_CONFIG}' if d.getVar('XEN_TEMPLATE_CONFIG') else ''} \
    "

do_configure[noexec] = "1"
do_compile[depends] += " image-builder-native:do_populate_sysroot"

do_compile(){
    ${RECIPE_SYSROOT_NATIVE}/usr/bin/uboot-script-gen -c ${WORKDIR}/${XEN_TEMPLATE_CONFIG} -s -d ${WORKDIR} -o ${WORKDIR}/xen_boot
}

do_install() {
	install -d ${D}/boot
	install -m 0644 ${WORKDIR}/xen_boot.scr ${D}/boot
}

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0644 ${WORKDIR}/xen_boot.scr ${DEPLOYDIR}
}

FILES:${PN} = "/boot/*"

addtask do_deploy after do_compile before do_build
