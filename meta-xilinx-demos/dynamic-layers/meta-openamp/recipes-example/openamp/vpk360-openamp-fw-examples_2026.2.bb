SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal VPK360 board."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal VPK360 board."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/versal-vpk360-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08090727/external/packagegroup-openamp-fw-examples/versal-vpk360-multidomain_packagegroup-openamp-fw-examples_08090727.tar.gz"
SRC_URI[sha256sum] = "2e21b1831c0308376a83000acb1109a050501355f82fa782f7ded0387fa72c1a"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "versal-vpk360"
