SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Series Gen 2 VEK385 (Rev B) evaluation \
kit."
DESCRIPTION = "Pre-built Cortex-R52 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Series Gen 2 VEK385 \
(Rev B) evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05190927/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_05190927.tar.gz"
SRC_URI[sha256sum] = "197a4d4167262fd786c63929a0af28f3b1e1c699619a56c9d8d5d2552d0ecaf8"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
