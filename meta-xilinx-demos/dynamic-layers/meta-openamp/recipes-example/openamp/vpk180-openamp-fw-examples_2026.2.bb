SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Premium vpk180 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Premium vpk180 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/versal-vpk180-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08140754/external/packagegroup-openamp-fw-examples/versal-vpk180-multidomain_packagegroup-openamp-fw-examples_08140754.tar.gz"
SRC_URI[sha256sum] = "3e9b116c9412d978df867c01f1bde78edfb122da3a176620225e2cb7adabe47f"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vpk180"
