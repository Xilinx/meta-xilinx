SUMMARY = "Packagegroup pulling in the Xen hypervisor runtime, \
xen-tools and supporting components used by the AMD Xilinx Xen-based \
virtualization stack."
DESCRIPTION = "Xen supported packages"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup features_check

REQUIRED_DISTRO_FEATURES = "xen"

XEN_EXTRA_PACKAGES = " \
	kernel-module-xen-blkback \
	kernel-module-xen-gntalloc \
	kernel-module-xen-gntdev \
	kernel-module-xen-netback \
	kernel-module-xen-wdt \
	xen \
	${@bb.utils.contains('DISTRO_FEATURES', 'efi', 'xen-efi', '', d)} \
	xen-tools \
	xen-tools-xenstat \
	${@bb.utils.contains('DISTRO_FEATURES', 'vmsep', 'qemu-aarch64 qemu-keymaps', 'qemu', d)} \
	"

RDEPENDS:${PN} = "${XEN_EXTRA_PACKAGES}"
