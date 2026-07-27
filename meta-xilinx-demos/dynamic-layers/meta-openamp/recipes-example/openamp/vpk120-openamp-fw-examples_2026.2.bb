SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Premium VPK120 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Premium VPK120 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/versal-vpk120-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/07261002/external/packagegroup-openamp-fw-examples/versal-vpk120-multidomain_packagegroup-openamp-fw-examples_07261002.tar.gz"
SRC_URI[sha256sum] = "d444ef4a7378fcf9863f4a0622cd53eecdaccf448acf4641f4e379ec1771288c"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vpk120"
