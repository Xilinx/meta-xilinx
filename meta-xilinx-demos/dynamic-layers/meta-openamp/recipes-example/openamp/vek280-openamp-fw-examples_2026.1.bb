SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal AI Edge VEK280 evaluation kit."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal AI Edge VEK280 \
evaluation kit."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/versal-vek280-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/06020328/external/packagegroup-openamp-fw-examples/versal-vek280-multidomain_packagegroup-openamp-fw-examples_06020328.tar.gz"
SRC_URI[sha256sum] = "3b32711bd203405b51a6fce1d996af20ee20257bd40084861e55be9b6a297f07"

require conf/includes/openamp-fw-example.inc
