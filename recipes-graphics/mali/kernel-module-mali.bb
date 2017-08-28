SUMMARY = "A Mali 400 Linux Kernel module"
SECTION = "kernel/modules"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = " \
	file://linux/license/gpl/mali_kernel_license.h;md5=1436c0d104589824163a3eb50fbb5050 \
	"

PV = "r7p0-00rel0"

SRC_URI = " \
	https://developer.arm.com/-/media/Files/downloads/mali-drivers/kernel/mali-utgard-gpu/DX910-SW-99002-${PV}.tgz \
	file://0001-Change-Makefile-to-be-compatible-with-Yocto.patch \
	file://0002-staging-mali-r7p0-00rel0-Add-the-ZYNQ-ZYNQMP-platfor.patch \
	file://0003-staging-mali-r7p0-00rel0-Remove-unused-trace-macros.patch \
	file://0004-staging-mali-r7p0-00rel0-Don-t-include-mali_read_phy.patch \
	file://0005-mali-r7p0-PAGE_CACHE_SHIFT-to-PAGE_CACHE.patch \
	file://0006-staging-mali-r7p0-page_cache_release-to-put_page.patch \
	file://0007-mali_memory_os_alloc.c-Align-with-dma_attrs-changes-.patch \
	file://0008-arm.c-dma_ops-will-be-modified-by-the-driver-only-ti.patch \
	file://0009-linux-mali_kernel_linux.c-Handle-clock-when-probed-a.patch \
	file://0010-common-mali_pm.c-Add-PM-runtime-barrier-after-removi.patch \
	"
SRC_URI[md5sum] = "db3ef3258eb55700484ecadfdce1fee1"
SRC_URI[sha256sum] = "496ba80684aa4236806891a8445978849f7dd07299f5e58b14d52cd5e7ba0536"

inherit module

do_make_scripts[depends] += "virtual/kernel:do_unpack"

S = "${WORKDIR}/DX910-SW-99002-${PV}/driver/src/devicedrv/mali"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_zynqmp = "zynqmp"

EXTRA_OEMAKE = 'KDIR="${STAGING_KERNEL_DIR}" \
		ARCH="${ARCH}" \
		BUILD=release \
		MALI_PLATFORM="arm" \
		USING_DT=1 \
		MALI_SHARED_INTERRUPTS=1 \
		CROSS_COMPILE="${TARGET_PREFIX}" \
		O=${STAGING_KERNEL_BUILDDIR} \
		MALI_QUIET=1 \
		'
