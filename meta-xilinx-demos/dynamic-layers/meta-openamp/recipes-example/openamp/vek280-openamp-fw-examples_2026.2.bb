SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal AI Edge VEK280 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal AI Edge VEK280 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/versal-vek280-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08090727/external/packagegroup-openamp-fw-examples/versal-vek280-multidomain_packagegroup-openamp-fw-examples_08090727.tar.gz"
SRC_URI[sha256sum] = "744cc7b0c146c270e9bdb6ef08c854ca56fff99c0f9f9d076cb1a20c00e5f17e"

require conf/includes/openamp-fw-example.inc
