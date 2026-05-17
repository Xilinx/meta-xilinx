SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Series Gen 2 VEK385 (Rev B) evaluation \
kit."
DESCRIPTION = "Pre-built Cortex-R52 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Series Gen 2 VEK385 \
(Rev B) evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05170553/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_05170553.tar.gz"
SRC_URI[sha256sum] = "addde7b543f7205a44432de8c1dd9bdea74d34ac90afd5770a0ae0032e57307b"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
