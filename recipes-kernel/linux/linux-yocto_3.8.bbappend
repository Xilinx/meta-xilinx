
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-yocto:"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"

require linux-machine-common.inc

SRC_URI_append_zynq = " file://xilinx-v14.5_modifications_to_v3.8.scc"
SRC_URI_append_zynq = " file://microblaze-patches_v3.8.scc"
SRC_URI_append_zynq   = " file://zynq-default-standard.scc"
SRC_URI_append_microblaze   = " file://zynq-microblaze-standard.scc"

SRC_URI_append += "git://github.com/Xilinx/xilinx-kernel-cache;protocol=git;branch=master;type=kmeta;name=externalcache;destsuffix=external-cache/"
SRCREV_externalcache = "${AUTOREV}"

COMPATIBLE_MACHINE_zynq = "zynq"
COMPATIBLE_MACHINE_microblaze = "microblaze"

