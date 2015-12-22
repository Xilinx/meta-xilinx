SUMMARY = "External Xilinx toolchain"
include xilinx-toolchain.inc

PV = "${XILINX_VER_MAIN}"
PKGV = "${XILINX_VER_GCC}"

PR = "r1"

LICENSE = "NCSA | MIT"
LIC_FILES_CHKSUM = "\
	file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420 \
	file://${COMMON_LICENSE_DIR}/NCSA;md5=1b5fdec70ee13ad8a91667f16c1959d7 \
	"

INHIBIT_DEFAULT_DEPS = "1"

DEPENDS = "gcc-runtime"

PROVIDES = "virtual/${TARGET_PREFIX}gcc-sanitizers gcc-sanitizers"

PACKAGES = "\
	libasan \
	libasan-dev \
	libasan-dbg \
	libasan-staticdev \
	libubsan \
	libubsan-dev \
	libubsan-dbg \
	libubsan-staticdev \
"

RDEPENDS_libasan = "libstdc++"
RDEPENDS_libubsan = "libstdc++"
RDEPENDS_libasan-dev = "${PN}"
RDEPENDS_libubsan-dev = "${PN}"
RRECOMMENDS_${PN} = "libasan libubsan"

FILES_${PN} = ""
FILES_${PN}-dev = ""
FILES_${PN}-staticdev = ""

FILES_libasan = "${libdir}/libasan.so.*"
FILES_libasan-dbg = "${libdir}/.debug/libasan.so.*"
FILES_libasan-dev = "\
	${libdir}/libasan_preinit.o \
	${libdir}/libasan.so \
	${libdir}/libasan.la \
"
FILES_libasan-staticdev = "${libdir}/libasan.a"

FILES_libubsan = "${libdir}/libubsan.so.*"
FILES_libubsan-dbg = "${libdir}/.debug/libubsan.so.*"
FILES_libubsan-dev = "\
	${libdir}/libubsan.so \
	${libdir}/libubsan.la \
"
FILES_libubsan-staticdev = "${libdir}/libubsan.a"

INSANE_SKIP_libasan = "build-deps ldflags"
INSANE_SKIP_libasan-dev = "build-deps ldflags"
INSANE_SKIP_libubsan = "build-deps ldflags"
INSANE_SKIP_libubsan-dev = "build-deps ldflags"

do_install() {
	root="${EXTERNAL_TOOLCHAIN}"

	install -d ${D}/${libdir}

	find $root \( -path "*/${XILINX_TARGET_SYS}/*/libasan.*" \
	-o -path "*/${XILINX_TARGET_SYS}/*/libasan_preinit.o" \
	-o -path "*/${XILINX_TARGET_SYS}/*/libusan*" \) \
	-exec cp -a {} ${D}/${libdir} \;
}
