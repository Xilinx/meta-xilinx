QEMU_TARGETS = "aarch64 arm microblaze microblazeel"

require recipes-devtools/qemu/qemu.inc

SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

LIC_FILES_CHKSUM = " \
		file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
		file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913 \
		"

# Xilinx release 2016.4
SRCREV = "4b90a13118b6e005d688d7aefb0900f7a67531df"
SRC_URI = "git://github.com/Xilinx/qemu.git;protocol=https;nobranch=1"

S = "${WORKDIR}/git"

PV = "2.6.0+git+${SRCPV}"

# Disable KVM completely
KVMENABLE = "--disable-kvm"

# Strip all appends (needed because qemu.inc adds patches using overrides)
SRC_URI[_append] = ""

DISABLE_STATIC_pn-qemu-xilinx-native = ""

# append a suffix dir, to allow multiple versions of QEMU to be installed
datadir_append = "/qemu-xilinx"
bindir_append = "/qemu-xilinx"
libexecdir_append = "/qemu-xilinx"

# ensure configure is passed the modified dirs
EXTRA_OECONF += " \
		--bindir=${bindir} \
		--datadir=${datadir} \
		--mandir=${mandir} \
		--docdir=${docdir} \
		"

do_install() {
	export STRIP="true"
	autotools_do_install
}
