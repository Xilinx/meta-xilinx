SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal RF vrk160 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal RF vrk160 evaluation \
kit."

LICENSE = "BSD-3-Clause"
BOARD_NAME = "versal-vrk160-multidomain_packagegroup-openamp-fw-examples"
LIC_FILES_CHKSUM = "file://${BOARD_NAME}/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"
SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/07280104/external/packagegroup-openamp-fw-examples/versal-vrk160-multidomain_packagegroup-openamp-fw-examples_07280104.tar.gz"
SRC_URI[sha256sum] = "af5adb172bfbdcc05b0c4390ddbb745b7d824e8ee70688b4b901d7d50b2ea350"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vrk160"
