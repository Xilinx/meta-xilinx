SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal RF VRK160 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal RF VRK160 evaluation \
kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2025.2/edf_files/2025.2/11150857/external/packagegroup-openamp-fw-examples/versal-vrk160-sdt-seg_packagegroup-openamp-fw-examples_11150857.tar.gz"
SRC_URI[sha256sum] = "20d879434d004e3e407e2a3664a09852561a95f74753818b0804490a7de1b2b8"

require conf/includes/openamp-fw-example.inc
