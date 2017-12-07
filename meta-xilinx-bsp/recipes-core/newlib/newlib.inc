SUMMARY = "Newlib is a C library intended for use on embedded systems"
HOMEPAGE = "https://sourceware.org/newlib/"
DESCRIPTION = "It is a conglomeration of several library parts, all under free software licenses that make them easily usable on embedded products."
SECTION = "libs"

LICENSE = "GPLv2 & LGPLv3 & GPLv3 & LGPLv2"
LIC_FILES_CHKSUM = " \
		file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
		file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6 \
		file://COPYING3;md5=d32239bcb673463ab874e80d47fae504 \
		file://COPYING.LIBGLOSS;md5=73f5c98779aea7dba4a6c94a74ab0ae2 \
		file://COPYING.LIB;md5=2d5025d4aa3495befef8f17206a5b0a1 \
		file://COPYING.NEWLIB;md5=bfdd6396bf3b02bd23ffbb9f7ddfbec6 \
		file://newlib/libc/posix/COPYRIGHT;md5=103468ff1982be840fdf4ee9f8b51bbf \
		file://newlib/libc/sys/linux/linuxthreads/LICENSE;md5=73640207fbc79b198c7ffd4ad4d97aa0 \
		"

SRC_URI = "ftp://sourceware.org/pub/newlib/newlib-${PV}.tar.gz"
SRC_URI[md5sum] = "767dc60aaf814d091997d91d588968b2"
SRC_URI[sha256sum] = "5b76a9b97c9464209772ed25ce55181a7bb144a66e5669aaec945aa64da3189b"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "virtual/${TARGET_PREFIX}gcc"

S = "${WORKDIR}/newlib-${PV}"
B = "${WORKDIR}/build"

# disable use of a link hash style
TARGET_LINK_HASH_STYLE_microblaze = ""

# disable stdlib
TARGET_CC_ARCH_append = " -nostdlib"

CONFIGUREOPTS = " \
		--build=${BUILD_SYS} \
		--host=${HOST_SYS} \
		--target=${TARGET_SYS} \
		--prefix=${prefix} \
		--exec-prefix=${exec_prefix} \
		--bindir=${bindir} \
		--libdir=${libdir} \
		--includedir=${includedir} \
		--enable-languages=c \
		--with-newlib \
		--with-gnu-as \
		--with-gnu-ld \
		--disable-multilib \
		"

do_configure[cleandirs] = "${B}"

do_install() {
	oe_runmake install DESTDIR='${D}'

	# output of headers/libs in 'tooldir' subdirectories, move it up to the prefix dir.
	mv ${D}${prefix}/${TARGET_SYS}/* ${D}${prefix}/
	rmdir ${D}${prefix}/${TARGET_SYS}
}

