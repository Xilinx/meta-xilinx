SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal RF vrk165 revB evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal RF vrk165 revB evaluation \
kit."

LICENSE = "BSD-3-Clause"
BOARD_NAME = "versal-vrk165-revb-multidomain_packagegroup-openamp-fw-examples"
LIC_FILES_CHKSUM = "file://${BOARD_NAME}/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"
SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08271500/external/packagegroup-openamp-fw-examples/versal-vrk165-revb-multidomain_packagegroup-openamp-fw-examples_08271500.tar.gz"
SRC_URI[sha256sum] = "eeada29d527626b82a8e0d904df2f1e198d99d9d0919957b6cd656d7d2b43f52"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vrk165-revb"
