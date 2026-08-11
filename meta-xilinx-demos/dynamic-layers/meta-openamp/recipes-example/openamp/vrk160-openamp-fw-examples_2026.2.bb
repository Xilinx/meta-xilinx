SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Prime VMK180 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Prime VMK180 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/versal-vrk160-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08101825/external/packagegroup-openamp-fw-examples/versal-vrk160-multidomain_packagegroup-openamp-fw-examples_08101825.tar.gz"
SRC_URI[sha256sum] = "20df18ce3edee0c46ffa2596dddf0c91e61985c55f9fddd8b573c873d8b16d96"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vrk160"
