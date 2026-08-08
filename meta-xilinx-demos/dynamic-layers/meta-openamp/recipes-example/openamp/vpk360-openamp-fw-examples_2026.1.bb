SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal VPK360 board."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal VPK360 board."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${S}/versal-vpk360-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08081523/external/packagegroup-openamp-fw-examples/versal-vpk360-multidomain_packagegroup-openamp-fw-examples_08081523.tar.gz"
SRC_URI[sha256sum] = "ee07ca62d3da84a66f6f05d50db4fcdd57b30ee086d1dd6e49b2dc491a469bc6"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vpk360"
