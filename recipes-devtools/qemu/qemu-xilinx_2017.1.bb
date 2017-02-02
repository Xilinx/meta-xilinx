QEMU_TARGETS ?= "aarch64 arm microblaze microblazeel"

require recipes-devtools/qemu/qemu.inc

SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

LIC_FILES_CHKSUM = " \
		file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
		file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913 \
		"

SRCREV = "a83265d7403ee49c9a911c920961ef29deac96eb"
SRC_URI = "git://github.com/Xilinx/qemu.git;protocol=https;nobranch=1 \
		"

S = "${WORKDIR}/git"

# Disable KVM completely
KVMENABLE = "--disable-kvm"

# Strip all appends (needed because qemu.inc adds patches using overrides)
SRC_URI[_append] = ""

DISABLE_STATIC_pn-qemu-xilinx = ""
DISABLE_STATIC_pn-qemu-xilinx-native = ""
DISABLE_STATIC_pn-nativesdk-qemu-xilinx = ""

PTEST_ENABLED = ""

# append a suffix dir, to allow multiple versions of QEMU to be installed
EXTRA_OECONF_append = " \
		--bindir=${bindir}/qemu-xilinx \
		--libexecdir=${libexecdir}/qemu-xilinx \
		--datadir=${datadir}/qemu-xilinx \
		"

do_install() {
	export STRIP="true"
	autotools_do_install

	# Prevent QA warnings about installed ${localstatedir}/run
	if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
