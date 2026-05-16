SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Prime VMK180 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Prime VMK180 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/versal-vmk180-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05160541/external/packagegroup-openamp-fw-examples/versal-vmk180-multidomain_packagegroup-openamp-fw-examples_05160541.tar.gz"
SRC_URI[sha256sum] = "86930d2c041b56c6ca4f05dffe0d496ad722a03c0a13783156fb2ec50b5b36ea"

require conf/includes/openamp-fw-example.inc
