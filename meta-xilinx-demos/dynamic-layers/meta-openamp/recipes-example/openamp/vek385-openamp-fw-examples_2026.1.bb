SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Series Gen 2 VEK385 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R52 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Series Gen 2 VEK385 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05190927/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo_05190927.tar.gz"
SRC_URI[sha256sum] = "97d8f5d07683e496ffd9d35cdf0bc0d0b1b3c0761984f5e738cef004544aa181"

OPENAMPFW_BOARD = "vek385"
require conf/includes/openamp-fw-example.inc
