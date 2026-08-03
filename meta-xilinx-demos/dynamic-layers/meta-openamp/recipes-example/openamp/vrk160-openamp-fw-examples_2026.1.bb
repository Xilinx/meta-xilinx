SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal RF vrk160 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal RF vrk160 evaluation \
kit."

LICENSE = "BSD-3-Clause"
BOARD_NAME = "versal-vrk160-multidomain_packagegroup-openamp-fw-examples"
LIC_FILES_CHKSUM = "file://${BOARD_NAME}/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"
SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08031517/external/packagegroup-openamp-fw-examples/versal-vrk160-multidomain_packagegroup-openamp-fw-examples_08031517.tar.gz"
SRC_URI[sha256sum] = "f6614045495314c59c708eb26afdb72e6e09d2577ff358d30259b91f593296a0"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vrk160"
