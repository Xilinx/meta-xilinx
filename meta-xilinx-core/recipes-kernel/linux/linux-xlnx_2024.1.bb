LINUX_VERSION = "6.1.0"
YOCTO_META ?= "git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.1;destsuffix=yocto-kmeta"
KBRANCH="master"
SRCREV = "5928e9762d9bd76b32521147b42a4102a7a6cb33"
SRCREV_meta = "185bcfcbe480c742247d9117011794c69682914f"

KCONF_AUDIT_LEVEL="0"

include linux-xlnx.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Workaround for:
#  rm: cannot remove '.../tmp/work/zynqmp_generic-xilinx-linux/linux-xlnx/6.6.0-xilinx-v2024.1+gitAUTOINC+340eed5001-r0/image/lib/modules/6.6.0-xilinx-v2024.1-g340eed500130/source': No such file or directory
# This will not be required Scarthgap
kernel_do_install:prepend () {
	mkdir -p "${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}"
	touch "${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/source"
}
