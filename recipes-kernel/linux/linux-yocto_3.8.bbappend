
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-yocto:"
FILESEXTRAPATHS_prepend := "${THISDIR}/linux-xlnx:"

require linux-machine-common.inc

SRC_URI_append += " \
        file://xilinx-v14.5_modifications_to_v3.8.scc \
        file://microblaze-patches_v3.8.scc \
        file://${KMACHINE}-standard.scc \
        "

SRC_URI_append += "git://github.com/Xilinx/xilinx-kernel-cache;protocol=git;branch=master;type=kmeta;name=externalcache;destsuffix=external-cache/"
SRCREV_externalcache = "${AUTOREV}"

COMPATIBLE_MACHINE = "zedboard-zynq7|zc702-zynq7|kc705-trd-microblazeel"
