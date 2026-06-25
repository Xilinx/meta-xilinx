SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal AI Edge VEK280 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal AI Edge VEK280 \
evaluation kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2025.2/edf_files/2025.2/11150857/external/packagegroup-openamp-fw-examples/versal-vek280-sdt-seg_packagegroup-openamp-fw-examples_11150857.tar.gz"
SRC_URI[sha256sum] = "369fa7acbad49819056dc4166d9d48de35671338499450b52daaadb7ab7bc62d"

require conf/includes/openamp-fw-example.inc
