SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

QEMU_TARGETS = "aarch64 arm microblaze microblazeel"

LIC_FILES_CHKSUM = " \
		file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
		file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913 \
		"
DEPENDS = "glib-2.0 zlib pixman"

XILINX_RELEASE_VERSION = "v2019.2"
XILINX_QEMU_VERSION ?= "v2.11.1"
BRANCH ?= "branch/xilinx-v2019.2"
SRCREV ?= "6617fbc8be3525ca524f7d4ef7fc7b14c5b0c822"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PV = "${XILINX_QEMU_VERSION}-xilinx-${XILINX_RELEASE_VERSION}+git${SRCPV}"
BRANCH ?= ""
REPO ?= "git://github.com/Xilinx/qemu.git;protocol=https"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

SRC_URI_append = " file://0001-The-glibc-2.29.9000-6.fc31.x86_64-package-finally-in.patch"

S = "${WORKDIR}/git"

EXTRA_OECONF_append= " --python=python2.7"

# Disable KVM completely
PACKAGECONFIG_remove = "kvm"
PACKAGECONFIG[ssh] = "--enable-libssh,,"

# Enable libgcrypt
PACKAGECONFIG_append = " gcrypt fdt alsa kvm"

DISABLE_STATIC_pn-${PN} = ""

PTEST_ENABLED = ""

# append a suffix dir, to allow multiple versions of QEMU to be installed
EXTRA_OECONF_append = " \
		--bindir=${bindir}/qemu-xilinx \
		--libexecdir=${libexecdir}/qemu-xilinx \
		"

do_configure_prepend() {
	# rewrite usage of 'libgcrypt-config' with 'pkg-config libgcrypt'
	sed -r -i 's/libgcrypt-config(\s*--)/pkg-config libgcrypt\1/g' ${S}/configure
}

do_install_append() {
	# Prevent QA warnings about installed ${localstatedir}/run
	if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
