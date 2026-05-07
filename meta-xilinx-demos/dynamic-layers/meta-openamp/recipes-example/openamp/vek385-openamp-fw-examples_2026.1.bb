SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Series Gen 2 VEK385 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R52 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Series Gen 2 VEK385 \
evaluation kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04280544/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo_04280544.tar.gz"
SRC_URI[sha256sum] = "733a41f2fabe22c0121cc76b0f2c466c67cf9f3c22268f77e05677c5764949ac"

OPENAMPFW_BOARD = "vek385"
require conf/includes/openamp-fw-example.inc
