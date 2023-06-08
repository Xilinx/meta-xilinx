SUMMARY = "DPDK Kernel Module igb_uio"
DESCRIPTION = "UIO driver for Intel IGB PCI cards"
HOMEPAGE = "http://git.dpdk.org/dpdk-kmods/"

FILESEXTRAPATHS:prepend := "${THISDIR}/dpdk:"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://igb_uio.c;beginline=1;endline=4;md5=a05cd72f85021e22ff6b2632b437450b"

SRC_URI = "git://dpdk.org/git/dpdk-kmods;protocol=https;branch=main \
           file://0001-support-5.18-kernel-ABI.patch;patchdir=../.. \
           "
SRCREV = "4a589f7bed00fc7009c93d430bd214ac7ad2bb6b"

S = "${WORKDIR}/git/linux/igb_uio"

PV = "1.0"

inherit module

EXTRA_OEMAKE += "KSRC='${STAGING_KERNEL_DIR}'"

do_install() {
    if [ -e "${S}/igb_uio.ko" ]
    then
        install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/
        install -m 0644 ${S}/igb_uio.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/
    fi
}
