SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD VEK386 board."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD VEK386 board."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${S}/versal-2ve-2vm-vek386-multidomain_openamp-zephyr-demo/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/07270924/external/openamp-zephyr-demo/versal-2ve-2vm-vek386-multidomain_openamp-zephyr-demo_07270924.tar.gz"
SRC_URI[sha256sum] = "52a2656415c9cd0414d7177ea2d432b064622e1f90cd7b9b6dabce46c1188fe4"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa78-common = "${MACHINE}"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vek386"
