SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal Series Gen 2 VEK385 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R52 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal Series Gen 2 VEK385 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.2/edf_files/2026.2/08130450/external/openamp-zephyr-demo/versal-2ve-2vm-vek385-multidomain_openamp-zephyr-demo_08130450.tar.gz"
SRC_URI[sha256sum] = "067b395a8b7a3e89ff2cba62f2de8e795547fe92edae2dd5f32d72f4d96dde4e"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:amd-cortexa78-common = "${MACHINE}"
COMPATIBLE_MACHINE:amd-cortexa78-mali-common = "${MACHINE}"

OPENAMPFW_BOARD = "vek385"
require conf/includes/openamp-fw-example.inc
