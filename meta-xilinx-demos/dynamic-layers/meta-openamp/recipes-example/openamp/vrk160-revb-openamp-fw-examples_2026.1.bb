SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal RF vrk160 revB evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal RF vrk160 revB evaluation \
kit."

LICENSE = "BSD-3-Clause"
BOARD_NAME = "versal-vrk160-revb-multidomain_packagegroup-openamp-fw-examples"
LIC_FILES_CHKSUM = "file://${BOARD_NAME}/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"
SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08242244/external/packagegroup-openamp-fw-examples/versal-vrk160-revb-multidomain_packagegroup-openamp-fw-examples_08242244.tar.gz"
SRC_URI[sha256sum] = "cbcdc2a0bc0d7d0661243be001ceed223e21894c12e7ce64dc379e53be98afa2"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vrk160-revb"
