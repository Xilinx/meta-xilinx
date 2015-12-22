SUMMARY = "External Xilinx toolchain"
include xilinx-toolchain.inc

PV = "${XILINX_VER_MAIN}"
PKGV = "${XILINX_VER_KERNEL}"

PR = "r1"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"

PROVIDES = "\
	linux-libc-headers \
	virtual/linux-libc-headers \
	"

PACKAGES = "\
	linux-libc-headers-dev \
	linux-libc-headers-dbg \
"

FILES_linux-libc-headers-dev = " \
	${includedir}/asm* \
	${includedir}/asm-generic \
	${includedir}/linux \
	${includedir}/mtd \
	${includedir}/rdma \
	${includedir}/scsi \
	${includedir}/sound \
	${includedir}/video \
	${includedir}/bits \
	${includedir}/drm \
"

EXTERNALPN = "linux-libc-headers"
RRECOMMENDS_${EXTERNALPN}-dbg = "${EXTERNALPN}-dev (= ${EXTENDPKGV})"
ALLOW_EMPTY_${EXTERNALPN}-dbg = "1"

INHIBIT_DEFAULT_DEPS = "1"
BBCLASSEXTEND = ""

do_install() {
	sysroot="${EXTERNAL_TOOLCHAIN_SYSROOT}"
	install -d ${D}${includedir}
	for d in asm asm-generic linux mtd rdma scsi sound video bits drm; do
		cp -a $sysroot/usr/include/$d ${D}${includedir}/$d
	done
	rm ${D}${includedir}/bits/syscall.h
	find ${D}${includedir} -name ..install.cmd | xargs rm -f
}

