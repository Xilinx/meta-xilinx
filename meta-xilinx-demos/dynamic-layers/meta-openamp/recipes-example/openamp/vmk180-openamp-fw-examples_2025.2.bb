SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Prime VMK180 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Prime VMK180 \
evaluation kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2025.2/edf_files/2025.2/11150857/external/packagegroup-openamp-fw-examples/versal-vmk180-sdt-seg_packagegroup-openamp-fw-examples_11150857.tar.gz"
SRC_URI[sha256sum] = "5689d23dab5f6e76b010dbb5246b06cf59810c3e488a267da7cac4bec982fe6c"

require conf/includes/openamp-fw-example.inc
