SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD VEK386 board."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD VEK386 board."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${S}/versal-2ve-2vm-vek386-multidomain_openamp-zephyr-demo/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/08210614/external/openamp-zephyr-demo/versal-2ve-2vm-vek386-multidomain_openamp-zephyr-demo_08210614.tar.gz"
SRC_URI[sha256sum] = "181cf945c275671fd818bd27adf6f187725e397f50dbb33c5397daef1d755588"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa78-common = "${MACHINE}"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vek386"
