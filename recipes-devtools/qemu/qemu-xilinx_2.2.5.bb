QEMU_TARGETS ?= "aarch64 arm microblaze microblazeel"

require recipes-devtools/qemu/qemu.inc

SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

LIC_FILES_CHKSUM = " \
		file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
		file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913 \
		"

# This is the commit used in the 2016.3 tag
SRCREV = "de453ab4cf993f63de502c4efd8a08a347c3b164"
SRC_URI = "git://github.com/Xilinx/qemu.git;protocol=https;nobranch=1"

S = "${WORKDIR}/git"

PV = "2.2.50+git+${SRCPV}"

# Disable KVM completely
KVMENABLE = "--disable-kvm"

# Strip all appends (needed because qemu.inc adds patches using overrides)
SRC_URI[_append] = ""

DISABLE_STATIC_pn-qemu-xilinx-native = ""

PACKAGECONFIG[quorum] = "--enable-quorum, --disable-quorum, gnutls,"
PACKAGECONFIG[vnc-tls] = "--enable-vnc --enable-vnc-tls,--disable-vnc-tls, gnutls,"
PACKAGECONFIG[vnc-ws] = "--enable-vnc --enable-vnc-ws,--disable-vnc-ws, gnutls,"
PACKAGECONFIG[gcrypt] = "--enable-libgcrypt,--disable-libgcrypt,libgcrypt,"
PACKAGECONFIG[nss] = ""
PACKAGECONFIG[nettle] = ""
PACKAGECONFIG[glx] = ""
PACKAGECONFIG[gnutls] = ""
PACKAGECONFIG[bzip2] = ""

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
