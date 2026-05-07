SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Series Gen 2 VEK385 (Rev B) evaluation \
kit."
DESCRIPTION = "Pre-built Cortex-R52 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Series Gen 2 VEK385 \
(Rev B) evaluation kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/04280544/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-revb-multidomain_openamp-zephyr-demo_04280544.tar.gz"
SRC_URI[sha256sum] = "17c9c5f51687654a007afbacd2b8577138733c96bfe78b01daf3bfa27a96377f"

OPENAMPFW_BOARD = "vek385-revb"
require conf/includes/openamp-fw-example.inc
