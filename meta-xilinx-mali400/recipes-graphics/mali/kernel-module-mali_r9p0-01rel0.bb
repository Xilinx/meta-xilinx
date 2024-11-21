SUMMARY = "A Mali 400 Linux Kernel module"
SECTION = "kernel/modules"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = " \
	file://linux/license/gpl/mali_kernel_license.h;md5=f5af2d61f4c1eb262cb6a557aaa1070a \
	"

SRC_URI = " \
	https://developer.arm.com/-/media/Files/downloads/mali-drivers/kernel/mali-utgard-gpu/DX910-SW-99002-${PV}.tgz \
	file://0001-Change-Makefile-to-be-compatible-with-Yocto.patch \
	file://0002-staging-mali-r8p0-01rel0-Add-the-ZYNQ-ZYNQMP-platfor.patch \
	file://0003-staging-mali-r8p0-01rel0-Remove-unused-trace-macros.patch \
	file://0004-staging-mali-r8p0-01rel0-Don-t-include-mali_read_phy.patch \
	file://0005-linux-mali_kernel_linux.c-Handle-clock-when-probed-a.patch \
	file://0006-arm.c-global-variable-dma_ops-is-removed-from-the-ke.patch \
	file://0010-common-mali_pm.c-Add-PM-runtime-barrier-after-removi.patch \
	file://0011-linux-mali_kernel_linux.c-Enable-disable-clock-for-r.patch\
	file://0012-linux-mali_memory_os_alloc-Remove-__GFP_COLD.patch\
	file://0013-linux-mali_memory_secure-Add-header-file-dma-direct..patch\
	file://0014-linux-mali_-timer-Get-rid-of-init_timer.patch\
	file://0015-fix-driver-failed-to-check-map-error.patch \
	file://0016-mali_memory_secure-Kernel-5.0-onwards-access_ok-API-.patch \
	file://0017-Support-for-vm_insert_pfn-deprecated-from-kernel-4.2.patch \
	file://0018-Change-return-type-to-vm_fault_t-for-fault-handler.patch \
	file://0019-get_monotonic_boottime-ts-deprecated-from-kernel-4.2.patch \
	file://0020-Fix-ioremap_nocache-deprecation-in-kernel-5.6.patch \
	file://0021-Use-updated-timekeeping-functions-in-kernel-5.6.patch \
	file://0022-Set-HAVE_UNLOCKED_IOCTL-default-to-true.patch \
	file://0023-Use-PTR_ERR_OR_ZERO-instead-of-PTR_RET.patch \
	file://0024-Use-community-device-tree-names.patch \
	file://0025-Import-DMA_BUF-module-and-update-register_shrinker-f.patch \
	file://0026-Fix-gpu-driver-probe-failure.patch \
	file://0027-Updated-clock-name-and-structure-to-match-LIMA-drive.patch \
	file://0028-Replace-vma-vm_flags-direct-modifications-with-modif.patch \
	file://0029-Fixed-buildpath-QA-warning.patch \
	"
SRC_URI[md5sum] = "85ea110dd6675c70b7d01af87ec9633c"
SRC_URI[sha256sum] = "7a67127341d17640c1fff5dad80258fb2a37c8a2121b81525fe2327e4532ce2b"

inherit features_check module

PARALLEL_MAKE = "-j 1"

S = "${WORKDIR}/DX910-SW-99002-${PV}/driver/src/devicedrv/mali"

REQUIRED_MACHINE_FEATURES = "mali400"

EXTRA_OEMAKE = 'KDIR="${STAGING_KERNEL_DIR}" \
		ARCH="${ARCH}" \
		BUILD=release \
		MALI_PLATFORM="arm" \
		USING_DT=1 \
		MALI_SHARED_INTERRUPTS=1 \
		CROSS_COMPILE="${TARGET_PREFIX}" \
		MALI_QUIET=1 \
		'
