SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal RF vrk165 revB evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal RF vrk165 revB evaluation \
kit."

LICENSE = "BSD-3-Clause"
BOARD_NAME = "versal-vrk165-revb-multidomain_packagegroup-openamp-fw-examples"
LIC_FILES_CHKSUM = "file://${BOARD_NAME}/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"
SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08311448/external/packagegroup-openamp-fw-examples/versal-vrk165-revb-multidomain_packagegroup-openamp-fw-examples_08311448.tar.gz"
SRC_URI[sha256sum] = "ba24a3e044052b49888b078e9958e30a9594fb8f9d07df52a9766c73aaad81c1"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vrk165-revb"
