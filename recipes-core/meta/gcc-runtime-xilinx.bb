SUMMARY = "External Xilinx toolchain"
include xilinx-toolchain.inc

PV = "${XILINX_VER_MAIN}"
PKGV = "${XILINX_VER_GCC}"

PR = "r1"

LICENSE = "GPL-3.0-with-GCC-exception & GPL-3.0"
LICENSE_gcc-runtime-dbg = "GPL-3.0-with-GCC-exception & GPL-3.0"
LICENSE_libstdc++ = "GPL-3.0-with-GCC-exception"
LICENSE_libstdc++-precompile-dev = "GPL-3.0-with-GCC-exception"
LICENSE_libstdc++-dev = "GPL-3.0-with-GCC-exception"
LICENSE_libstdc++-staticdev = "GPL-3.0-with-GCC-exception"
LICENSE_libssp = "GPL-3.0-with-GCC-exception"
LICENSE_libssp-dev = "GPL-3.0-with-GCC-exception"
LICENSE_libssp-staticdev = "GPL-3.0-with-GCC-exception"
LICENSE_libatomic = "GPL-3.0-with-GCC-exception"
LICENSE_libatomic-dev = "GPL-3.0-with-GCC-exception"
LICENSE_libatomic-staticdev = "GPL-3.0-with-GCC-exception"
LIC_FILES_CHKSUM = "\
	file://${COMMON_LICENSE_DIR}/GPL-3.0-with-GCC-exception;md5=aef5f35c9272f508be848cd99e0151df \
	file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891 \
	"

DEPENDS = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++ libgcc"
PROVIDES = "virtual/${TARGET_PREFIX}compilerlibs"

INHIBIT_DEFAULT_DEPS = "1"

PACKAGES = "\
	gcc-runtime-dbg \
	libstdc++ \
	libstdc++-dev \
	libstdc++-staticdev \
	libssp \
	libssp-dev \
	libssp-staticdev \
	libatomic \
	libatomic-dev \
	libatomic-staticdev \
"

# The base package doesn't exist, so we clear the recommends.
RRECOMMENDS_gcc-runtime-dbg = ""

FILES_gcc-runtime-dbg = "${libdir}/.debug/"

FILES_libstdc++ = "${libdir}/libstdc++.so.*"
FILES_libstdc++-dev = "\
	${includedir}/c++/ \
	${libdir}/libstdc++.so \
	${libdir}/libstdc++.la \
	${libdir}/libsupc++.la \
"
FILES_libstdc++-staticdev = "\
	${libdir}/libstdc++.a \
	${libdir}/libsupc++.a \
"

FILES_libssp = "${libdir}/libssp.so.*"
FILES_libssp-dev = "\
	${libdir}/libssp*.so \
	${libdir}/libssp*_nonshared.a \
	${libdir}/libssp*.la \
	${libdir}/gcc/${XILINX_TARGET_SYS}/${XILINX_VER_GCC}/include/ssp \
"
FILES_libssp-staticdev = "${libdir}/libssp*.a"

FILES_libatomic = "${libdir}/libatomic.so.*"
FILES_libatomic-dev = "\
	${libdir}/libatomic.so \
	${libdir}/libatomic.la \
"
FILES_libatomic-staticdev = "${libdir}/libatomic.a"

INSANE_SKIP_${PN}-dev = "ldflags"
INSANE_SKIP_libstdc++ = "ldflags"
INSANE_SKIP_libstdc++-dev = "ldflags"
INSANE_SKIP_libssp = "ldflags"
INSANE_SKIP_libatomic = "ldflags"
INSANE_SKIP_libatomic-dev = "ldflags"

do_install() {
	root="${EXTERNAL_TOOLCHAIN}"
	sysroot="${EXTERNAL_TOOLCHAIN_SYSROOT}"

	install -d ${D}/${libdir} \
		${D}/${includedir} \
		${D}/${libdir}/gcc/${XILINX_TARGET_SYS}/${XILINX_VER_GCC}/include/ssp

	find $root \( -path "*/${XILINX_TARGET_SYS}/*/libstdc++.*" \
		-o -path "*/${XILINX_TARGET_SYS}/*/libatomic.*" \
		-o -path "*/${XILINX_TARGET_SYS}/*/libssp*" \
		-o -path "*/${XILINX_TARGET_SYS}/*/libsupc++.a" \) \
		-exec cp -a {} ${D}/usr/lib \;

	cp -a ${sysroot}/../include/. ${D}/${includedir}

	find $root -path "*/${XILINX_TARGET_SYS}/*/include/ssp/*" \
	-exec cp -a {} ${D}/usr/lib/gcc/${XILINX_TARGET_SYS}/${XILINX_VER_GCC}/include/ssp \;

	# Move some of the libs in /lib to /usr/lib
	for i in libstdc++ libssp libatomic; do
		if [ -e ${D}${base_libdir}/$i.so ]; then
			mv ${D}${base_libdir}/$i.* ${D}${libdir}/
		fi
	done
}

do_package[depends] = "virtual/${MLPREFIX}libc:do_packagedata"
