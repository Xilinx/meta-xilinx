SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal VPK360 board."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal VPK360 board."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://versal-2vp-vpk360-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/09021406/external/packagegroup-openamp-fw-examples/versal-2vp-vpk360-multidomain_packagegroup-openamp-fw-examples_09021406.tar.gz"
SRC_URI[sha256sum] = "4105b32b2f4089dc9a9e7bb202ab40b8b209425602e049d25c61056ac8021467"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "vpk360"
