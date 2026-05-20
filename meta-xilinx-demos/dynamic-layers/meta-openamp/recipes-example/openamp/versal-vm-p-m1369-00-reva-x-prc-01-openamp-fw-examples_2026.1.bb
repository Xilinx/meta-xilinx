SUMMARY = "OpenAMP RPU firmware demo payloads (echo, matrix multiply, \
rpc_demo) for the AMD Versal VM-P-M1369-00 Rev A SOM on the X-PRC-01 \
carrier."
DESCRIPTION = "Pre-built Cortex-R5 OpenAMP firmware demo payloads \
(echo, matrix-multiply, rpc_demo) that the APU-side OpenAMP example \
applications load onto the RPU on the AMD Versal VM-P-M1369-00 Rev A \
SOM mounted on the X-PRC-01 carrier."

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/versal-vm-p-m1369-00-reva-x-prc-01-reva-multidomain_packagegroup-openamp-fw-examples/LICENSE.md;md5=0b96a4c07d631aa5141bd3f058ba43b0"

SRC_URI = "https://edf.amd.com/sswreleases/rel-v2026.1/edf_files/2026.1/05200705/external/packagegroup-openamp-fw-examples/versal-vm-p-m1369-00-reva-x-prc-01-reva-multidomain_packagegroup-openamp-fw-examples_05200705.tar.gz"
SRC_URI[sha256sum] = "c0d649e0b5d99e21505ea9fc554b743c9a9585a04cb369920375f5894fa01368"

require conf/includes/openamp-fw-example.inc
OPENAMPFW_BOARD = "versal-vm-p-m1369-00-reva-x-prc-01"
