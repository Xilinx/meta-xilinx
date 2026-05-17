SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal RF VRK165 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal RF VRK165 evaluation \
kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/versal-vrk165-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05170553/external/packagegroup-openamp-fw-examples/versal-vrk165-multidomain_packagegroup-openamp-fw-examples_05170553.tar.gz"
SRC_URI[sha256sum] = "0d58f529d93fb4397606e766533d2f45459037917a6b3b7a2a7246e1e7f5e354"

require conf/includes/openamp-fw-example.inc
