SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal RF VRK165 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal RF VRK165 evaluation \
kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05080630/external/packagegroup-openamp-fw-examples/versal-vrk165-multidomain_packagegroup-openamp-fw-examples_05080630.tar.gz"
SRC_URI[sha256sum] = "53ea3df2760d09dea8f6ed22be90fd29d38368cb84d15698d33898e47580a33d"

require conf/includes/openamp-fw-example.inc
