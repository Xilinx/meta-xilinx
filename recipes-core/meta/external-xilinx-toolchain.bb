require recipes-core/glibc/glibc-package.inc

INHIBIT_DEFAULT_DEPS = "1"

# License applies to this recipe code, not the toolchain itself
SUMMARY = "External Xilinx toolchain"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

DEPENDS += "${@base_conditional('PREFERRED_PROVIDER_linux-libc-headers', PN, '', 'linux-libc-headers', d)}"
PROVIDES += "\
	linux-libc-headers \
	virtual/${TARGET_PREFIX}gcc \
	virtual/${TARGET_PREFIX}g++ \
	virtual/${TARGET_PREFIX}gcc-initial \
	virtual/${TARGET_PREFIX}gcc-intermediate \
	virtual/${TARGET_PREFIX}binutils \
	virtual/${TARGET_PREFIX}libc-for-gcc \
	virtual/${TARGET_PREFIX}libc-initial \
	virtual/${TARGET_PREFIX}compilerlibs \
	virtual/libc \
	virtual/libintl \
	virtual/libiconv \
	virtual/linux-libc-headers \
	glibc-thread-db \
	libgcc \
	glibc \
	"

PV = "${CSL_VER_MAIN}"
PR = "r1"

SRC_URI = "file://SUPPORTED"

do_install() {
	# Use optimized files if available
	sysroot="${EXTERNAL_TOOLCHAIN_SYSROOT}"
	dbgroot="${EXTERNAL_TOOLCHAIN_DBGROOT}"

	cp -a $sysroot${base_libdir}/. ${D}${base_libdir}
	cp -a $sysroot/sbin/. ${D}${base_sbindir}
	
	install -d ${D}/usr
	for usr_element in bin libexec sbin share ${base_libdir}; do
		# Copy files from both the sysroot and the debugroot if they exist
		if [ ! -z "$sysroot" -a -e $sysroot/usr/$usr_element ]; then
			cp -a $sysroot/usr/$usr_element ${D}/usr/
		fi
		if [ ! -z "$dbgroot" -a -e $dbgroot/usr/$usr_element ]; then
			cp -a $dbgroot/usr/$usr_element ${D}/usr/
		fi
	done

	# Copy Include files
	cp -a $sysroot/usr/include/. ${D}${includedir}

	# strip out any multi-lib files (they are not supported)
	for element in bs m ldscripts; do
		if [ -e ${D}${libdir}/$element ]; then
			rm -rf ${D}${libdir}/$element
		fi
		if [ -e ${D}${base_libdir}/$element ]; then
			rm -rf ${D}${base_libdir}/$element
		fi
	done

	# Clean up the image (remove files and directories that are not packaged)
	## ${D}${sysconfdir}
	for i in ${D}/usr/share/zoneinfo ${D}/usr/lib/bin ${D}/usr/libexec ; do
		if [ -e $i ]; then
			rm -rf $i
		fi
	done

	# Move some of the libs in /lib to /usr/lib
	for i in libstdc++ libssp libatomic; do
		if [ -e ${D}${base_libdir}/$i.so ]; then
			mv ${D}${base_libdir}/$i.* ${D}${libdir}/
		fi
	done

	sed -i -e 's/__packed/__attribute__ ((packed))/' ${D}${includedir}/mtd/ubi-user.h
	sed -i -e "s# ${base_libdir}# ../..${base_libdir}#g" -e "s# ${libdir}# .#g" ${D}${libdir}/libc.so
	sed -i -e "s# ${base_libdir}# ../..${base_libdir}#g" -e "s# ${libdir}# .#g" ${D}${libdir}/libpthread.so
}

PACKAGES =+ " \
		libgcc libgcc-dev \
		libssp libssp-dev libssp-staticdev \
		libatomic libatomic-dev libatomic-staticdev \
		libstdc++ libstdc++-dev libstdc++-staticdev \
		linux-libc-headers linux-libc-headers-dev \
		gdbserver gdbserver-dbg \
		"

RDEPENDS_ldd += "bash"
RDEPENDS_tzcode += "bash"

# This test should be fixed to ignore .a files in .debug dirs
INSANE_SKIP_${PN}-dbg = "staticdev"

# We don't care about GNU_HASH in prebuilt binaries
INSANE_SKIP_${PN}-utils += "ldflags"
INSANE_SKIP_${PN}-dev += "ldflags"
INSANE_SKIP_libstdc++ += "ldflags"
INSANE_SKIP_libgcc += "ldflags"
INSANE_SKIP_libssp += "ldflags"
INSANE_SKIP_libatomic += "ldflags"
INSANE_SKIP_gdbserver += "ldflags"

PKG_${PN} = "glibc"
PKG_${PN}-dev = "glibc-dev"
PKG_${PN}-staticdev = "glibc-staticdev"
PKG_${PN}-doc = "glibc-doc"
PKG_${PN}-dbg = "glibc-dbg"
PKG_${PN}-pic = "glibc-pic"
PKG_${PN}-utils = "glibc-utils"
PKG_${PN}-gconv = "glibc-gconv"
PKG_${PN}-extra-nss = "glibc-extra-nss"
PKG_${PN}-thread-db = "glibc-thread-db"
PKG_${PN}-pcprofile = "glibc-pcprofile"

PKGV = "${CSL_VER_LIBC}"
PKGV_libssp = "${CSL_VER_GCC}"
PKGV_libssp-dev = "${CSL_VER_GCC}"
PKGV_libssp-staticdev = "${CSL_VER_GCC}"
PKGV_libatomic = "${CSL_VER_GCC}"
PKGV_libatomic-dev = "${CSL_VER_GCC}"
PKGV_libatomic-staticdev = "${CSL_VER_GCC}"
PKGV_libgcc = "${CSL_VER_GCC}"
PKGV_libgcc-dev = "${CSL_VER_GCC}"
PKGV_libstdc++ = "${CSL_VER_GCC}"
PKGV_libstdc++-dev = "${CSL_VER_GCC}"
PKGV_libstdc++-staticdev = "${CSL_VER_GCC}"
PKGV_linux-libc-headers = "${CSL_VER_KERNEL}"
PKGV_linux-libc-headers-dev = "${CSL_VER_KERNEL}"
PKGV_gdbserver = "${CSL_VER_GDB}"
PKGV_gdbserver-dbg = "${CSL_VER_GDB}"

FILES_libssp = "${libdir}/libssp.so.*"
FILES_libssp-dev = "${libdir}/libssp.so ${libdir}/libssp_nonshared.a ${libdir}/libssp_nonshared.la"
FILES_libssp-staticdev = "${libdir}/libssp.a ${libdir}/libssp.la"
FILES_libatomic = "${libdir}/libatomic.so.*"
FILES_libatomic-dev = "${libdir}/libatomic.so"
FILES_libatomic-staticdev = "${libdir}/libatomic.a ${libdir}/libatomic.la"
FILES_libgcc = "${base_libdir}/libgcc_s.so.1"
FILES_libgcc-dev = "${base_libdir}/libgcc_s.so"
FILES_libstdc++ = "${libdir}/libstdc++.so.*"
FILES_libstdc++-dev = " \
	${includedir}/c++/${PV} \
	${libdir}/libstdc++.so \
	${libdir}/libstdc++.la \
	${libdir}/libsupc++.la \
	"
FILES_libstdc++-staticdev = "${libdir}/libstdc++.a ${libdir}/libsupc++.a"
FILES_linux-libc-headers = " \
	${includedir}/asm* \
	${includedir}/linux \
	${includedir}/mtd \
	${includedir}/rdma \
	${includedir}/scsi \
	${includedir}/sound \
	${includedir}/video \
	"
FILES_gdbserver = "${bindir}/gdbserver"
FILES_gdbserver-dbg = "${bindir}/.debug/gdbserver"

CSL_VER_MAIN ??= ""

python () {
    if not d.getVar("CSL_VER_MAIN", True):
        raise bb.parse.SkipPackage("External toolchain not configured (CSL_VER_MAIN not set).")
}
