SUMMARY = "AMD/Xilinx DPDK poll-mode drivers (PMD)"
DESCRIPTION = "Out-of-tree DPDK platform-bus poll-mode drivers (PMD) for \
AMD/Xilinx Ethernet IP. Currently ships the XXV Ethernet + AXI DMA PMD."

HOMEPAGE = "https://github.com/Xilinx/ethernet-pmd"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e4d251193b2b7c9b41b04ea3f16e329e"

BRANCH = "main"
SRC_URI = "git://github.com/Xilinx/ethernet-pmd.git;protocol=https;branch=${BRANCH}"
SRCREV = "650e2a62b01059dc3c7b3c654675a4815101d27c"

DEPENDS = "dpdk"

COMPATIBLE_HOST = "aarch64.*-linux"

inherit meson pkgconfig

# The PMD installs into DPDK's RTE_EAL_PMD_PATH, ${libdir}/dpdk/pmds-<abi>/,
# where <abi> is the ABI version of the DPDK it is built against. That value
# is only available from the DPDK sysroot at configure time, after this
# recipe has been parsed, so the path is matched with a wildcard.
FILES:${PN} += "${libdir}/dpdk/pmds-*/*.so*"
INSANE_SKIP:${PN} += "dev-so"
