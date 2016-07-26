QEMU_TARGETS ?= "aarch64 arm microblaze microblazeel"

require recipes-devtools/qemu/qemu.inc

SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

LIC_FILES_CHKSUM = " \
               file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
               file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913 \
               "

SRCREV = "27014cf0dee92dcb43691d441f0f36cfc0d0f3a7"
SRC_URI = "git://github.com/Xilinx/qemu.git;protocol=https;nobranch=1  \
               "

S = "${WORKDIR}/git"

# Disable KVM completely
KVMENABLE = "--disable-kvm"

# Strip all appends (needed because qemu.inc adds patches using overrides)
SRC_URI[_append] = ""

PACKAGECONFIG[quorum] = "--enable-quorum, --disable-quorum, gnutls,"
PACKAGECONFIG[vnc-tls] = "--enable-vnc --enable-vnc-tls,--disable-vnc-tls, gnutls,"
PACKAGECONFIG[vnc-ws] = "--enable-vnc --enable-vnc-ws,--disable-vnc-ws, gnutls,"
PACKAGECONFIG[gcrypt] = "--enable-libgcrypt,--disable-libgcrypt,libgcrypt,"
PACKAGECONFIG[nss] = ""
PACKAGECONFIG[nettle] = ""
PACKAGECONFIG[glx] = ""
PACKAGECONFIG[gnutls] = ""
PACKAGECONFIG[bzip2] = ""

do_install() {
    export STRIP="true"
    autotools_do_install

    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}

do_compile_ptest() {
	:
}

do_install_ptest() {
	:
}
