SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Prime VMK180 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Prime VMK180 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/versal-vmk180-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08030529/external/packagegroup-openamp-fw-examples/versal-vmk180-multidomain_packagegroup-openamp-fw-examples_08030529.tar.gz"
SRC_URI[sha256sum] = "ee0b6e22e2ac3da1ef5ec036b636ef48807a086a1d08f42a92de73388c7fd87e"

require conf/includes/openamp-fw-example.inc
