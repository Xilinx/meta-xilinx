require recipes-core/glibc/glibc-package.inc
SUMMARY = "External Xilinx toolchain"
include xilinx-toolchain.inc

PV = "${XILINX_VER_MAIN}"
PKGV = "${XILINX_VER_LIBC}"

PR = "r1"

LICENSE = "GPLv2 & LGPL-2.1"
LIC_FILES_CHKSUM = " \
	file://${COREBASE}/meta/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe \
	file://${COMMON_LICENSE_DIR}/LGPL-2.1;md5=1a6d268fd218675ffea8be556788b780 \
	"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "virtual/${TARGET_PREFIX}binutils linux-libc-headers"

PROVIDES += "\
	glibc \
	virtual/${TARGET_PREFIX}libc-for-gcc \
	virtual/${TARGET_PREFIX}libc-initial \
	virtual/libc \
	virtual/libintl \
	virtual/libiconv \
	glibc-thread-db \
	"
S = "${WORKDIR}"

SRC_URI = " \
	file://SUPPORTED \
	file://nscd.init \
	file://nscd.conf \
	file://nscd.service \
	"

RDEPENDS_ldd += "bash"
RDEPENDS_tzcode += "bash"

# This test should be fixed to ignore .a files in .debug dirs
INSANE_SKIP_${PN}-dbg = "staticdev"
INSANE_SKIP_${PN} += "build-deps"
# We don't care about GNU_HASH in prebuilt binaries
INSANE_SKIP_${PN}-utils += "build-deps ldflags"
INSANE_SKIP_${PN}-dev += "ldflags"

PKG_${PN} = "glibc"
RPROVIDES_${PN} += "${TCLIBC} glibc"
PKG_${PN}-dbg = "glibc-dbg"
RPROVIDES_${PN}-dbg += "${TCLIBC}-dbg glibc-dbg"
PKG_${PN}-dev = "glibc-dev"
RPROVIDES_${PN}-dev += "${TCLIBC}-dev glibc-dev"
PKG_${PN}-doc = "glibc-doc"
RPROVIDES_${PN}-doc += "${TCLIBC}-doc glibc-doc"
PKG_${PN}-extra-nss = "glibc-extra-nss"
RPROVIDES_${PN}-extra-nss += "${TCLIBC}-extra-nss glibc-extra-nss"
PKG_${PN}-gconv = "glibc-gconv"
RPROVIDES_${PN}-gconv += "${TCLIBC}-gconv glibc-gconv"
PKG_${PN}-mtrace = "glibc-mtrace"
RPROVIDES_${PN}-mtrace += "${TCLIBC}-mtrace glibc-mtrace"
PKG_${PN}-pcprofile = "glibc-pcprofile"
RPROVIDES_${PN}-pcprofile += "${TCLIBC}-pcprofile glibc-pcprofile"
PKG_${PN}-pic = "glibc-pic"
RPROVIDES_${PN}-pic += "${TCLIBC}-pic glibc-pic"
PKG_${PN}-staticdev = "glibc-staticdev"
RPROVIDES_${PN}-staticdev += "${TCLIBC}-staticdev glibc-staticdev"
PKG_${PN}-thread-db = "glibc-thread-db"
RPROVIDES_${PN}-thread-db += "${TCLIBC}-thread-db glibc-thread-db"
PKG_${PN}-utils = "glibc-utils"
RPROVIDES_${PN}-utils += "${TCLIBC}-utils glibc-utils"

FILES_${PN} += " /lib/${XILINX_TARGET_SYS} /usr/lib/${XILINX_TARGET_SYS}"

do_install() {
	# Use optimized files if available
	sysroot="${EXTERNAL_TOOLCHAIN_SYSROOT}"
	root="${EXTERNAL_TOOLCHAIN}"

	install -d ${D}/lib
	set -f
	for ele in ${libc_baselibs} ${FILES_libsegfault} \
		${FILES_libcidn} ${FILES_libmemusage} \
		${FILES_glibc-extra-nss} ${FILES_glibc-thread-db} \
		${FILES_${PN}-pcprofile} ; do
		ele=$(echo $ele |  sed -e "s%^/lib.*/%%")
		find $root -name "$ele" -exec cp -a {} ${D}/lib \;
	done
	set +f

	find $root -path "*/${XILINX_TARGET_SYS}/*/ld*.so*" -exec cp -a {} ${D}/lib \;

	cp -a $sysroot/sbin/. ${D}${base_sbindir}

	install -d ${D}/usr
	for usr_element in bin libexec sbin share ${base_libdir}; do
		if [ ! -z "$sysroot" -a -e $sysroot/usr/$usr_element ]; then
			cp -a $sysroot/usr/$usr_element ${D}/usr/
		fi
	done

	if [ "${XILINX_TARGET_SYS}" == "aarch64-linux-gnu" ] ||  \
	   [ "${XILINX_TARGET_SYS}" == "arm-linux-gnueabihf" ]; then

		if [ -d ${D}/usr/lib/arm-linux-gnueabi ]; then
			rm -rf ${D}/usr/lib/arm-linux-gnueabi
	        fi
                if [ -h ${D}/lib/ld-linux.so.3 ]; then
                        rm ${D}/lib/ld-linux.so.3
                fi

		linker_name="${@base_contains("TUNE_FEATURES", "aarch64", "ld-linux-aarch64.so.1", "ld-linux-armhf.so.3",d)}"
		file=$(basename $(readlink -m ${D}/lib/${linker_name}))
		rm ${D}/lib/$linker_name
		ln -s ./$file ${D}/lib/$linker_name

		mv ${D}/usr/lib/${XILINX_TARGET_SYS}/* ${D}/usr/lib
		rm -r ${D}/usr/lib/${XILINX_TARGET_SYS}
		for link in $(find ${D}/usr/lib -type l); do
			file=$(basename $(readlink -m ${link}))
			rm $link
			ln -s ../../lib/$file $link
		done
		ln -s . ${D}/usr/lib/${XILINX_TARGET_SYS}
		ln -s . ${D}/lib/${XILINX_TARGET_SYS}
		rm ${D}/usr/lib/*.map
		rm -r ${D}/usr/lib/libc_pic
	fi

	find ${D}/usr/ \( -path "*/usr/lib/libstdc++*" \
		-o -path "*/usr/lib/libasan*" \
		-o -path "*/usr/lib/libubsan*" \
		-o -path "*/usr/lib/libatomic*" \) \
		-exec rm {} \;

	cp -a $sysroot/usr/include/. ${D}${includedir}

	for d in xen asm asm-generic linux mtd rdma scsi sound video bits drm; do
		rm -rf "${D}${includedir}/$d"
	done
	mkdir -p ${D}${includedir}/bits
	cp -a $sysroot/usr/include/bits/syscall.h ${D}${includedir}/bits/syscall.h

	# strip out any multi-lib files (they are not supported)
	for element in bs m ldscripts; do
		if [ -e ${D}${libdir}/$element ]; then
			rm -rf ${D}${libdir}/$element
		fi
		if [ -e ${D}${base_libdir}/$element ]; then
			rm -rf ${D}${base_libdir}/$element
		fi
	done

	if [ "${XILINX_TARGET_SYS}" == "arm-xilinx-linux-gnueabi" ]; then
		if [ -e ${D}${libdir}/locale ]; then
			rm -rf ${D}${libdir}/locale
		fi
	fi

	# Clean up the image (remove files and directories that are not packaged)
	for i in ${D}/usr/share/zoneinfo ${D}/usr/lib/bin ${D}/usr/libexec ; do
		if [ -e $i ]; then
			rm -rf $i
		fi
	done

	if [ -f ${D}${libdir}/libc.so ];then
		sed -i -e "s# ${base_libdir}# ../..${base_libdir}#g" \
			-e "s#/${XILINX_TARGET_SYS}##g" \
			-e "s# ${libdir}# .#g" ${D}${libdir}/libc.so
	fi
	if [ -f ${D}${libdir}/libpthread.so ];then
		sed -i -e "s# ${base_libdir}# ../..${base_libdir}#g" \
			-e "s#/${XILINX_TARGET_SYS}##g" \
			-e "s# ${libdir}# .#g" ${D}${libdir}/libpthread.so
	fi

	find  ${D}/usr/lib  -name "*crt*.o" -exec chmod -x {} \;
	find  ${D}/usr/lib  -name "*.a" -exec chmod -x {} \;

	install -d ${S}/nscd
	install -m 0755 ${WORKDIR}/nscd.conf ${S}/nscd/nscd.conf
	install -m 0755 ${WORKDIR}/nscd.init ${S}/nscd/nscd.init
	install -m 0755 ${WORKDIR}/nscd.service ${S}/nscd/nscd.service
}

do_package[depends] += "${MLPREFIX}libgcc:do_packagedata"
do_package_write_ipk[depends] += "${MLPREFIX}libgcc:do_packagedata"
do_package_write_deb[depends] += "${MLPREFIX}libgcc:do_packagedata"
do_package_write_rpm[depends] += "${MLPREFIX}libgcc:do_packagedata"
