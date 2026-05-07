SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Premium VPK120 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Premium VPK120 \
evaluation kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2025.2/edf_files/2025.2/11150857/external/packagegroup-openamp-fw-examples/versal-vpk120-sdt-seg_packagegroup-openamp-fw-examples_11150857.tar.gz"
SRC_URI[sha256sum] = "07d01dec131d68089aacedbc4479d97167a091166ee63d3eb812513202e8bd43"

require conf/includes/openamp-fw-example.inc
