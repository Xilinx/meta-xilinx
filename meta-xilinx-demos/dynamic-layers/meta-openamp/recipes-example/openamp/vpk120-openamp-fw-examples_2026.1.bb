SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Premium VPK120 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Premium VPK120 \
evaluation kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05080630/external/packagegroup-openamp-fw-examples/versal-vpk120-multidomain_packagegroup-openamp-fw-examples_05080630.tar.gz"
SRC_URI[sha256sum] = "5fd42671a5b06ff01ff44a55246a17fbcba50ed05f0bcc46e0660404861e3f61"

require conf/includes/openamp-fw-example.inc
