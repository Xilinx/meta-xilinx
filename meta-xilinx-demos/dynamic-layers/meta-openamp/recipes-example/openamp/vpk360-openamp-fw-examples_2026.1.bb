SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal VPK360 board."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal VPK360 board."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${S}/versal-vpk360-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08071440/external/packagegroup-openamp-fw-examples/versal-vpk360-multidomain_packagegroup-openamp-fw-examples_08071440.tar.gz"
SRC_URI[sha256sum] = "76b205e0b070ab625503d2e76a3238c776483155acc2e5133b316352071929eb"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vpk360"
