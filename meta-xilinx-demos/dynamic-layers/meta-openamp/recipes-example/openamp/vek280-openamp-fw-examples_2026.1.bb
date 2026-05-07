SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal AI Edge VEK280 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal AI Edge VEK280 \
evaluation kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05080630/external/packagegroup-openamp-fw-examples/versal-vek280-multidomain_packagegroup-openamp-fw-examples_05080630.tar.gz"
SRC_URI[sha256sum] = "1f2ec8ebac79b026a84bca7ae38a237b8d45b30400667e33588934287fe6f950"

require conf/includes/openamp-fw-example.inc
