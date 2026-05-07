SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Series Gen 2 VEK385 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R52 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Series Gen 2 VEK385 \
evaluation kit."
LICENSE = "CLOSED"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2025.2/edf_files/2025.2/11150857/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-sdt-seg_openamp-zephyr-demo_11150857.tar.gz"
SRC_URI[sha256sum] = "a2387de23a7e50db41ba30581290d9aa903262dfccf71dbb29b4bab7631a48cb"

require conf/includes/openamp-fw-example.inc
