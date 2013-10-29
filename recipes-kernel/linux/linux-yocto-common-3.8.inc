# linux patches
LINUX_XLNX_PATCHES = "file://xilinx-v14.5_modifications_to_v3.8.scc \
                      file://microblaze-patches_v3.8.scc \
                      file://xlnx_3.8_modifications_to_v14.5.scc \
                     "
SRC_URI_append_zynq = " ${LINUX_XLNX_PATCHES}"
SRC_URI_append_microblaze = " ${LINUX_XLNX_PATCHES}"

SRCREV_externalcache_zynq = "c1ddc3a3baddeec5f5d5a5d37448670be8aab7bd"
SRCREV_externalcache_microblaze = "c1ddc3a3baddeec5f5d5a5d37448670be8aab7bd"

