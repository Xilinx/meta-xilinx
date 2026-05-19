SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Premium VPK120 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Premium VPK120 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/versal-vpk120-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05190927/external/packagegroup-openamp-fw-examples/versal-vpk120-multidomain_packagegroup-openamp-fw-examples_05190927.tar.gz"
SRC_URI[sha256sum] = "9d599acaffc45be46e06e7ac8adc558bd5833d9900bf7bac0cc5ea63b37d4758"

require conf/includes/openamp-fw-example.inc
