SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal AI Core VCK190 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal AI Core VCK190 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/versal-vck190-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/06030659/external/packagegroup-openamp-fw-examples/versal-vck190-multidomain_packagegroup-openamp-fw-examples_06030659.tar.gz"
SRC_URI[sha256sum] = "9fd651b77f980bb584a8aaa41d344011d8b2d2b85b7e3233252252ac3acd33d7"

require conf/includes/openamp-fw-example.inc
