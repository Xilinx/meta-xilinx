
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-yocto:"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"

require linux-machine-common.inc

# External Cache
XLNX_EXTERNAL_CACHE = "git://github.com/Xilinx/xilinx-kernel-cache;protocol=git;branch=master;type=kmeta;name=externalcache;destsuffix=external-cache/"

SRC_URI_append_zynq += "${XLNX_EXTERNAL_CACHE}"
SRC_URI_append_microblaze += "${XLNX_EXTERNAL_CACHE}"

COMPATIBLE_MACHINE_zynq = "zynq"
COMPATIBLE_MACHINE_microblaze = "microblaze"

# default architecture configuration
SRC_URI_append_zynq   = " file://zynq-default-tiny.scc"
SRC_URI_append_microblaze   = " file://microblaze-default-tiny.scc"

