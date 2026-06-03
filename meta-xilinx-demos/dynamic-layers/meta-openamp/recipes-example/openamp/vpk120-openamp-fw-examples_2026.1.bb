SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Premium VPK120 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Premium VPK120 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/versal-vpk120-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/06030659/external/packagegroup-openamp-fw-examples/versal-vpk120-multidomain_packagegroup-openamp-fw-examples_06030659.tar.gz"
SRC_URI[sha256sum] = "99a7faaacaad4a888078c3df2d3d3e70486345f74c2b6435578c0617facd3abe"

require conf/includes/openamp-fw-example.inc
