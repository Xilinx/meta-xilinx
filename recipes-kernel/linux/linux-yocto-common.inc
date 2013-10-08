
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-yocto:"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"

require linux-machine-common.inc

# linux patches
LINUX_XLNX_PATCHES = "file://xilinx-v14.5_modifications_to_v3.8.scc file://microblaze-patches_v3.8.scc"
SRC_URI_append_zynq = " ${LINUX_XLNX_PATCHES}"
SRC_URI_append_microblaze = " ${LINUX_XLNX_PATCHES}"

# External Cache
XLNX_EXTERNAL_CACHE = "git://github.com/Xilinx/xilinx-kernel-cache;protocol=git;branch=master;type=kmeta;name=externalcache;destsuffix=external-cache/"

SRC_URI_append_zynq += "${XLNX_EXTERNAL_CACHE}"
SRC_URI_append_microblaze += "${XLNX_EXTERNAL_CACHE}"
SRCREV_externalcache_zynq = "c1ddc3a3baddeec5f5d5a5d37448670be8aab7bd"
SRCREV_externalcache_microblaze = "c1ddc3a3baddeec5f5d5a5d37448670be8aab7bd"

COMPATIBLE_MACHINE_zynq = "zynq"
COMPATIBLE_MACHINE_microblaze = "microblaze"

