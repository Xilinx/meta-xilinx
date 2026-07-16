SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal RF VRK165 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal RF VRK165 evaluation \
kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/versal-vrk165-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/07160732/external/packagegroup-openamp-fw-examples/versal-vrk165-multidomain_packagegroup-openamp-fw-examples_07160732.tar.gz"
SRC_URI[sha256sum] = "ab9897b7ab97ac466cb9b73b7da3fa8641a5942f4b1f13a953a9a0ad3e2f6ab0"

require conf/includes/openamp-fw-example.inc
