SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal AI Core VCK190 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal AI Core VCK190 \
evaluation kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2025.2/edf_files/2025.2/11150857/external/packagegroup-openamp-fw-examples/versal-vck190-sdt-seg_packagegroup-openamp-fw-examples_11150857.tar.gz"
SRC_URI[sha256sum] = "31ae7d1d0902875a7770c905c0356dbdbbbb4db3f965486abad24692a7a38810"

require conf/includes/openamp-fw-example.inc
