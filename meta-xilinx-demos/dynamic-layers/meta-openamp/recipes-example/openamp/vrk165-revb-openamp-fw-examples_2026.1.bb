SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal RF vrk165 revB evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal RF vrk165 revB evaluation \
kit."

LICENSE = "BSD-3-Clause"
BOARD_NAME = "versal-vrk165-revb-multidomain_packagegroup-openamp-fw-examples"
LIC_FILES_CHKSUM = "file://${BOARD_NAME}/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"
SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08301913/external/packagegroup-openamp-fw-examples/versal-vrk165-revb-multidomain_packagegroup-openamp-fw-examples_08301913.tar.gz"
SRC_URI[sha256sum] = "fc867d2daa74b86a37d49403a960033c07306ba2083a8001002f34310e7d3fdc"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vrk165-revb"
