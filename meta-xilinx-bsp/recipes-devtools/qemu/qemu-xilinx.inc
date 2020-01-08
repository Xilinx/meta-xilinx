SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

QEMU_TARGETS = "aarch64 arm microblaze microblazeel"

LIC_FILES_CHKSUM = " \
		file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
		file://COPYING.LIB;endline=24;md5=8c5efda6cf1e1b03dcfd0e6c0d271c7f \
		"
DEPENDS = "glib-2.0 zlib pixman"

XILINX_RELEASE_VERSION = "v2020.1"
XILINX_QEMU_VERSION ?= "v4.1.50"
BRANCH ?= "master-next"
SRCREV ?= "${AUTOREV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PV = "${XILINX_QEMU_VERSION}-xilinx-${XILINX_RELEASE_VERSION}+git${SRCPV}"
BRANCH ?= ""
REPO ?= "git://github.com/Xilinx/qemu.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

SRC_URI_append = " file://0010-configure-Add-pkg-config-handling-for-libgcrypt.patch"

S = "${WORKDIR}/git"

# Disable KVM completely
PACKAGECONFIG_remove = "kvm"
PACKAGECONFIG_append = " fdt"

# Enable libgcrypt
PACKAGECONFIG[gcrypt] = "--enable-gcrypt,--disable-gcrypt,libgcrypt,"
PACKAGECONFIG[fdt] = "--enable-fdt,--disable-fdt,dtc"

DISABLE_STATIC_pn-${PN} = ""

PTEST_ENABLED = ""

# append a suffix dir, to allow multiple versions of QEMU to be installed
EXTRA_OECONF_append = " \
		--bindir=${bindir}/qemu-xilinx \
		--libexecdir=${libexecdir}/qemu-xilinx \
		"

do_install_append() {
	# Prevent QA warnings about installed ${localstatedir}/run
	if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
