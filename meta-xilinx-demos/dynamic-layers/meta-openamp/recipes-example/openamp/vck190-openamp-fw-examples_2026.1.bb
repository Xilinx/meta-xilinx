SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal AI Core VCK190 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal AI Core VCK190 \
evaluation kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05080630/external/packagegroup-openamp-fw-examples/versal-vck190-multidomain_packagegroup-openamp-fw-examples_05080630.tar.gz"
SRC_URI[sha256sum] = "57557220ceeb01bc2b688ad0551a6c7311981d729478e2599677d0cd8cf0d67e"

require conf/includes/openamp-fw-example.inc
